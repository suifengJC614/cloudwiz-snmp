package cn.cloudwiz.dalian.snmp.exporter.smdb;

import cn.cloudwiz.dalian.commons.utils.HttpClient;
import cn.cloudwiz.dalian.commons.utils.HttpException;
import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.DeviceType;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.SnmpVersion;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.config.RemoteConfig;
import cn.cloudwiz.dalian.snmp.config.TokenConfig;
import cn.cloudwiz.dalian.snmp.exporter.RemoteService;
import cn.cloudwiz.dalian.snmp.exporter.SnmpService;
import com.cloudmon.alert.common.datamodel.MetricName;
import com.cloudmon.alert.common.exception.NoTokenFoundException;
import com.cloudmon.alert.common.opentsdb.OpentsdbDataPoint;
import com.cloudmon.alert.common.opentsdb.OpentsdbProxy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.TargetAware;
import org.springframework.data.web.JsonPath;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SMDBRemoteService implements RemoteService {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private RemoteConfig config;
    private Map<String, String> itemHeaders = new HashMap<>();
    private byte[] itemBody;

    @PostConstruct
    public void init() throws SQLException {
        Charset charset = Charset.forName("UTF-8");
        Properties properties = new Properties();
        // opentsdb
        properties.setProperty("metrics_server_url", config.getTsdbPath());//tsdb服务地址
        TokenConfig token = config.getToken();
        byte[] encodeBase64 = Base64.encodeBase64(token.getPassword().getBytes(charset));
        String s = new String(encodeBase64);
        properties.setProperty("mysql_username", token.getUsername());
        properties.setProperty("mysql_password", new String(encodeBase64));// 密码需要base64加密
        properties.setProperty("mysql_url", token.getUrl());// OrgTokenMap.java中获取token需要检索的数据库
        OpentsdbProxy.initInstance(properties);

        itemHeaders.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("type", "SNMPMonitor");
        itemBody = JsonUtils.toJson(body).getBytes(charset);
    }

    @Override
    public List<? extends MonitorDevice> getMonitorDevices() throws IOException {
        HttpClient client = HttpClient.getHttpClient();
        try {
            String respJson = client.get(config.getDevicePath());
            RemoteResponse response = proxyFactory.createProjection(RemoteResponse.class, respJson);
            return response.getMonitorDevices();
        } catch (HttpException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<? extends MonitorItem> getMonitorItem(MonitorDevice device) throws IOException {
        Assert.isInstanceOf(CMDBMonitorDevice.class, device, "getMonitorItem from cmdb, device type must be CMDBMonitorDevice");
        CMDBMonitorDevice cmdbDevice = (CMDBMonitorDevice) device;
        HttpClient client = HttpClient.getHttpClient();
        try {
            String respJson = client.post(config.getItemsPath(), itemBody, itemHeaders);
            RemoteResponse response = proxyFactory.createProjection(RemoteResponse.class, respJson);
            Set<String> netType = Stream.of(DeviceType.values()).map(DeviceType::getCode).collect(Collectors.toSet());
            String nodeType = cmdbDevice.getNodeType();
            if(netType.contains(nodeType)){
                nodeType = "NET_DEVICE";
            }
            return response.getMonitorItems(cmdbDevice.getBrand(), nodeType);
        } catch (HttpException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void sendSnmpResult(MonitorDevice device, List<? extends MonitorItem> items, Map<String, Number> result) throws IOException {
        OpentsdbProxy tsdb = OpentsdbProxy.getInstance();
        List<OpentsdbDataPoint> dps = items.stream().filter(item -> result.containsKey(item.getOid())).map(item -> {
            Map<String, String> tags = new HashMap<>();
            tags.put("org", Long.toString(config.getOrgKey()));
            tags.put("host", device.getDeviceName());
            OpentsdbDataPoint point = new OpentsdbDataPoint();
            String metricName = MetricName.createFullName(config.getOrgKey().toString(), config.getSysKey().toString(), item.getSaveKey());
            point.setMetric(metricName);
            point.setTags(tags);
            point.setTimestamp(System.currentTimeMillis() / 1000);
            point.setValue(result.get(item.getOid()).doubleValue());
            return point;
        }).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(dps)){
            try {
                tsdb.write(dps);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    public interface RemoteResponse {

        @JsonPath("$.result[?(@.ip != null && @.nodeType != null && @.brand != null)]")
        public List<CMDBMonitorDevice> getMonitorDevices();

        @JsonPath("$[?(@.key =~ /.*#{args[0]}@#{args[1]}/i)]")
        public List<CMDBMonitorItem> getMonitorItems(String brand, String noteType);

    }

    public interface CMDBMonitorItem extends MonitorItem {
        @Value("#{null}")
        public Long getKey();

        @JsonPath("$.attributes[?(@.name == 'oid')].value")
        public String getOid();

        @JsonPath("$.attributes[?(@.name == 'index')].value")
        public String getSaveKey();
    }

    public interface CMDBMonitorDevice extends CommunityDevice, TargetAware {

        @Value("#{null}")
        public Long getKey();

        @JsonPath("$.ip")
        public String getAddress();

        @JsonPath("$.snmpPort")
        public Integer getPort();

        default SnmpVersion getVersion() {
            Map<?, ?> target = (Map<?, ?>) getTarget();
            String result = (String) target.get("snmpVersion");
            for (SnmpVersion version : SnmpVersion.values()) {
                if (Objects.equals(version.getJdbcValue().toString(), result)) {
                    return version;
                }
            }
            return null;
        }

        @JsonPath("$.community")
        public String getCommunity();

        @JsonPath("$.nodeType")
        public String getNodeType();

        @JsonPath("$.brand")
        public String getBrand();

        @JsonPath("$.name")
        public String getDeviceName();
    }

    @Override
    public boolean isPause() {
        return false;
    }
}

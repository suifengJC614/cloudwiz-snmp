package cn.cloudwiz.dalian.snmp.exporter.smdb;

import cn.cloudwiz.dalian.commons.utils.HttpClient;
import cn.cloudwiz.dalian.commons.utils.HttpException;
import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.SnmpVersion;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.exporter.RemoteService;
import cn.cloudwiz.dalian.snmp.exporter.SnmpService;
import com.cloudmon.alert.common.datamodel.MetricName;
import com.cloudmon.alert.common.exception.NoTokenFoundException;
import com.cloudmon.alert.common.opentsdb.OpentsdbDataPoint;
import com.cloudmon.alert.common.opentsdb.OpentsdbProxy;
import org.apache.commons.codec.binary.Base64;
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

@Service
public class SMDBRemoteService implements RemoteService {

    @Autowired
    private ProjectionFactory proxyFactory;

    private String devicePath = "http://123.206.91.96:8080/_alertd/node_manager/node/ip?token=0f320e7db4a2d8ba0a3229753bf7c90d821479da";

    private String itemPath = "http://123.206.91.96:8080/_cmdb/ci/1/1/search2";
    private String tsdbPath = "http://123.206.91.96:8080/_tsdb";
    private String tokenUsername = "CloudInsight";
    private String tokenPassword = "Cloud";
    private String tokenUrl = "jdbc:mysql://123.206.91.96:3308/grafana";
    private Long orgKey = 1L;
    private Long sysKey = 1L;
    private Map<String, String> itemHeaders = new HashMap<>();
    private byte[] itemBody;

    @PostConstruct
    public void init() throws SQLException {
        Charset charset = Charset.forName("UTF-8");
        Properties properties = new Properties();
        // opentsdb
        properties.setProperty("metrics_server_url", tsdbPath);//tsdb服务地址
        byte[] encodeBase64 = Base64.encodeBase64(tokenPassword.getBytes(charset));
        String s = new String(encodeBase64);
        properties.setProperty("mysql_username", tokenUsername);
        properties.setProperty("mysql_password", new String(encodeBase64));// 密码需要base64加密
        properties.setProperty("mysql_url", tokenUrl);// OrgTokenMap.java中获取token需要检索的数据库
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
            String respJson = client.get(devicePath);
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
            String respJson = client.post(itemPath, itemBody, itemHeaders);
            RemoteResponse response = proxyFactory.createProjection(RemoteResponse.class, respJson);
            return response.getMonitorItems(cmdbDevice.getBrand(), cmdbDevice.getNodeType());
        } catch (HttpException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void sendSnmpResult(MonitorDevice device, List<? extends MonitorItem> items, Map<String, String> result) throws IOException {
        OpentsdbProxy tsdb = OpentsdbProxy.getInstance();
        List<OpentsdbDataPoint> dps = items.stream().filter(item -> result.containsKey(item.getOid())).map(item -> {
            Map<String, String> tags = new HashMap<>();
            tags.put("org", Long.toString(orgKey));
            tags.put("host", device.getAddress());

            OpentsdbDataPoint point = new OpentsdbDataPoint();
            String metricName = MetricName.createFullName(orgKey.toString(), sysKey.toString(), item.getSaveKey());
            point.setMetric(metricName);
            point.setTags(tags);
            point.setTimestamp(System.currentTimeMillis() / 1000);
            return point;
        }).collect(Collectors.toList());

        try {
            tsdb.write(dps);
        } catch (Exception e) {
            throw new IOException(e);
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
    }

    public interface MockMonitorDevice extends CommunityDevice {

        @Value("192.168.0.101")
        public String getAddress();

        @Value("161")
        public Integer getPort();

        @Value("VERSION_2C")
        public SnmpVersion getVersion();

        @Value("public")
        public String getCommunity();

    }

    public interface MockMonitorItem extends MonitorItem {

        @Value("1.3.6.1.2.1.1.5.0")
        public String getOid();

        @Value("net.if.out.errors")
        public String getSaveKey();
    }

    @Override
    public boolean isPause() {
        return false;
    }
}

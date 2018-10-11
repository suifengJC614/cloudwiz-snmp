package cn.cloudwiz.dalian.snmp.nms;

import com.cloudmon.alert.common.cmdb.CMDBProxy;
import com.cloudmon.alert.common.http.HttpClient;
import com.cloudmon.alert.common.http.HttpResult;
import com.cloudmon.alert.common.opentsdb.OpentsdbProxy;
import com.cloudmon.common.datamodel.cmdb.CI;
import com.cloudmon.common.datamodel.cmdb.CIType;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestCMService {

    public static void main(String[] args) throws IOException, SQLException {
//        InputStream is = TestCMService.class.getResourceAsStream("cloudmon.conf");
//        properties.load(is);
        // 设置属性文件
        final Properties properties = new Properties();
        // cmservice
        properties.setProperty("cmdb_service", "http://123.206.91.96:8080/_cmdb");
        // opentsdb
        properties.setProperty("metrics_server_url", "http://123.206.91.96:8080/_tsdb");
        byte[] encodeBase64 = Base64.encodeBase64("Cloud".getBytes("UTF-8"));
        String s = new String(encodeBase64);
        properties.setProperty("mysql_host", "123.206.91.96");
        properties.setProperty("mysql_port", "3308");
        properties.setProperty("mysql_username", "CloudInsight");
        properties.setProperty("mysql_password", new String(encodeBase64));
        properties.setProperty("mysql_url", "jdbc:mysql://123.206.91.96:3308/grafana");

        // 初始化
        HttpClient client = HttpClient.getInstance();
        client.init(properties);
        CMDBProxy.initialize(properties);
        TestCMService test = new TestCMService();
        // 获取CI数据
        test.getCIByKey(1, 1, CIType.Device, "172.21.16.14_VM1614centos_device_ram0");
        // 创建CI数据
//        test.createCI(9, 1, CIType.Device, "172.21.16.14_VM1614centos_device_ram0");

        // 写OpenTSDb
//        OpentsdbProxy.initInstance(properties);
//        test.writeOpenTsDB(1, 1);
    }

    public void getCIByKey(long orgId, long sysId, CIType type, String key) {
        CI ci = CMDBProxy.getCIByKey(orgId, sysId, type, key);
        System.out.print(ci.toString());
    }

    public void createCI(long orgId, long sysId, CIType type, String key) {
        CI ci = new CI();
        ci.setOrgId(orgId);
        ci.setSysId(sysId);
        ci.setKey(key);
        ci.setType(type);
        ci.addAttribute("devicename", "HP");
        ci.addAttribute("ip", "192.196.1.2");
        ci.addAttribute("deviceType", "核心路由器");
        HttpResult result = CMDBProxy.createCI(orgId, sysId, ci);
        System.out.print(result.toString());
    }

    public void writeOpenTsDB(long orgId, long sysId) {

        Map<String, String> tags = new HashMap<>();
        tags.put("org", Long.toString(orgId));
        tags.put("name", "abc");
        OpentsdbProxy.getInstance().writeOneDataPoint(
                orgId,
                sysId,
                "net.if.out.errors",
                System.currentTimeMillis()/1000,
                9999,
                tags
        );

    }
}



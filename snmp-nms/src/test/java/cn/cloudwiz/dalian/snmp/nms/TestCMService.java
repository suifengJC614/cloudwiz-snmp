package cn.cloudwiz.dalian.snmp.nms;

import com.cloudmon.alert.common.cmdb.CMDBProxy;
import com.cloudmon.alert.common.http.HttpClient;
import com.cloudmon.alert.common.http.HttpResult;
import com.cloudmon.alert.common.opentsdb.OpentsdbProxy;
import com.cloudmon.common.datamodel.cmdb.Attribute;
import com.cloudmon.common.datamodel.cmdb.CI;
import com.cloudmon.common.datamodel.cmdb.CIType;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class TestCMService {

    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
//        InputStream is = TestCMService.class.getResourceAsStream("cloudmon.conf");
//        properties.load(is);
        // 设置属性文件
        final Properties properties = new Properties();
        // cmservice
        properties.setProperty("cmdb_service", "http://123.206.91.96:8080/_cmdb");//cmservice服务地址
        // opentsdb
        properties.setProperty("metrics_server_url", "http://123.206.91.96:8080/_tsdb");//tsdb服务地址
        byte[] encodeBase64 = Base64.encodeBase64("Cloud".getBytes("UTF-8"));
        String s = new String(encodeBase64);
        properties.setProperty("mysql_username", "CloudInsight");
        properties.setProperty("mysql_password", new String(encodeBase64));// 密码需要base64加密
        properties.setProperty("mysql_url", "jdbc:mysql://123.206.91.96:3308/grafana");// OrgTokenMap.java中获取token需要检索的数据库。

        // 初始化
        HttpClient client = HttpClient.getInstance();
        client.init(properties);
        CMDBProxy.initialize(properties);
        TestCMService test = new TestCMService();
        // 获取CI数据
//        test.getCIByKey(1, 1, CIType.Device, "172.21.16.14_VM1614centos_device_ram0");
        // 获取多条CI数据
        Map<String,String> filters = new HashMap<>();
        filters.put("ip", "192.196.1.3");//filters对应着attribute的键值对，全检索时filters赋空或null
        test.searchCI(9, 1, CIType.Device, filters);
        // 更新CI数据
//        test.updateCI(9, 1, CIType.Device, "172.21.16.14_VM1614centos_device_ram0");
        // 创建CI数据
//        test.createCI(9, 1, CIType.Device, "172.21.16.14_VM1614centos_device_ram0");

        // 写OpenTSDb
//        System.out.println("begin tsdb init");
//        OpentsdbProxy.initInstance(properties);
//        // 因为initInstance方法中异步检索token信息，如果不sleep会导致写数据的时候缺少token而失败。实际工程中可在工程启动时init
//        Thread.sleep(5000);
//        System.out.println("writeOpenTsDB");
//        test.writeOpenTsDB(1, 1);
    }

    public void getCIByKey(long orgId, long sysId, CIType type, String key) {
        CI ci = CMDBProxy.getCIByKey(orgId, sysId, type, key);
        System.out.print(ci.toString());
    }

    public void searchCI(long orgId, long sysId, CIType type, Map<String,String> filters) {
        List<CI> ciList = CMDBProxy.searchCI(orgId, sysId, type, filters);
        System.out.print(ciList.size());
    }

    public void createCI(long orgId, long sysId, CIType type, String key) {
        CI ci = new CI();
        ci.setOrgId(orgId);
        ci.setSysId(sysId);
        ci.setKey(key);
        ci.setType(type);
        ci.addAttribute("devicename", "HP");
        ci.addAttribute("ip", "192.196.1.2");
        ci.addAttribute("deviceType", "Core Router");
        HttpResult result = CMDBProxy.createCI(orgId, sysId, ci);
        System.out.print(result.toString());
    }

    public void updateCI(long orgId, long sysId, CIType type, String key) {

        CI ci = CMDBProxy.getCIByKey(orgId, sysId, type, key);

        System.out.print("Updating CI ：" + ci.getId());

        List<Attribute> attributes = new LinkedList<>();
        attributes.add(new Attribute("devicename", "HP"));
        attributes.add(new Attribute("ip", "192.196.1.3"));
        attributes.add(new Attribute("deviceType", "哈哈"));

        CMDBProxy.updateCI(orgId, sysId, ci, attributes);
    }

    public void writeOpenTsDB(long orgId, long sysId) {

        Map<String, String> tags = new HashMap<>();
        tags.put("org", Long.toString(orgId));
        tags.put("host", "192.168.1.11"); // host项目必须有，使用机器IP
        tags.put("deviceId", "1");// 比如获取风扇转速数据的时候，一台机器有多个风扇，需要一个tag来区别不同风扇。
        OpentsdbProxy.getInstance().writeOneDataPoint(
                orgId,
                sysId,
                "net.if.out.errors",// 该字段为自定义监控项中设置的指标字段
                System.currentTimeMillis()/1000,
                8888,
                tags
        );
    }
}



package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.commons.utils.PageFactory;
import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.DeviceParams;
import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class DeviceTest {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private DeviceService devicebs;
    @Value("classpath:test_data.json")
    private Resource testJson;

    @Test
    public void page() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        DeviceParams params = testData.getDeviceParams();
        Page<MonitorDevice> result = devicebs.getMonitorDevices(params, Pageable.unpaged());
        System.out.println(result);
        result.forEach(item->{
            System.out.println(JsonUtils.toJson(item));
        });
    }

    @Ignore
    @Test
    public void get(){
        MonitorDevice device = devicebs.getMonitorDevice(1L);
        System.out.println(JsonUtils.toJson(device));
    }

    @Ignore
    @Test
    public void save() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        CommunityDevice device = testData.getMonitorDevice();
        Long savedKey = devicebs.save(device);
        System.out.println(String.format("saved success, saved key is %s", savedKey));
    }

}

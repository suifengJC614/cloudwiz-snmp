package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class DeviceTest {

    @Autowired
    private DeviceService devicebs;
    @Autowired
    private ProjectionFactory proxyFactory;
    @Value("classpath:test_data.json")
    private Resource testJson;

    @Test
    public void createDevice() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        CommunityDevice device = testData.getCommunityDevice();
    }

    public interface TestData {

        public CommunityDevice getCommunityDevice();

    }

}

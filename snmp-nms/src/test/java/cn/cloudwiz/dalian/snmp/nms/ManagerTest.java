package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.nms.SnmpManager;
import org.junit.Ignore;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class ManagerTest {

    @Autowired
    private SnmpManager manager;
    @Autowired
    private ProjectionFactory proxyFactory;
    @Value("classpath:test_data.json")
    private Resource testJson;

    @Ignore
    @Test
    public void snmpGet() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        CommunityDevice device = testData.getCommunityDevice();
        List<String> oids = Arrays.asList(
                "1.3.6.1.2.1.1.1.0",
                "1.3.6.1.2.1.1.2.0",
                "1.3.6.1.2.1.1.5.0"
        );
        Map<String, String> result = manager.get(device, oids);
        result.forEach((k, v) -> {
            System.out.println(String.format("%s = %s", k, v));
        });
    }


    @Test
    public void snmpWalk() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        CommunityDevice device = testData.getCommunityDevice();
        List<String> oids = Arrays.asList(
                "1.3.6.1.2.1.1",
                "1.3.6.1.2.1.2"
        );
        Map<String, String> result = manager.walk(device, oids);
        result.forEach((k, v) -> {
            System.out.println(String.format("%s = %s", k, v));
        });
    }

    public interface TestData {

        public CommunityDevice getCommunityDevice();

    }

}

package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemParams;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemsService;
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
public class MonitorItemTest {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private MonitorItemsService itemsbs;
    @Value("classpath:test_data.json")
    private Resource testJson;

    @Test
    public void find() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        MonitorItemParams params = testData.getMonitorItemParams();
        Page<MonitorItem> result = itemsbs.getMonitorItems(params, Pageable.unpaged());
        System.out.println(result);
        result.forEach(item -> {
            System.out.println(JsonUtils.toJson(item));
        });
    }

    @Ignore
    @Test
    public void get() {
        MonitorItem item = itemsbs.getMonitorItem(1L);
        System.out.println(JsonUtils.toJson(item));
    }

    @Ignore
    @Test
    public void save() throws IOException {
        TestData testData = proxyFactory.createProjection(TestData.class, testJson.getInputStream());
        MonitorItem item = testData.getMonitorItem();
        Long savedKey = itemsbs.save(item);
        System.out.println(String.format("save success, saved key is %s", savedKey));
    }

}

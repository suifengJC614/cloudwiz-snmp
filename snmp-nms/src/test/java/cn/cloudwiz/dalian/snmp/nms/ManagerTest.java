package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.snmp.api.nms.SnmpManager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class ManagerTest {

    @Autowired
    private SnmpManager manager;

    @Ignore
    @Test
    public void snmpGet() throws IOException {
        manager.get();
    }

    @Test
    public void snmpWalk() throws IOException {
        manager.walk();
    }

}

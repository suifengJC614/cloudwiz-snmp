package cn.cloudwiz.dalian.snmp;

import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class DeviceTest {

    @Autowired
    private DeviceService devicebs;

    @Test
    public void getMonitorDevice(){
        List<MonitorDevice> device = devicebs.getMonitorDevice();
        System.out.println(device);
    }

}

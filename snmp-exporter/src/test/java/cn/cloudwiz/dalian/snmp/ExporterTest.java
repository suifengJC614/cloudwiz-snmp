package cn.cloudwiz.dalian.snmp;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.SnmpVersion;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.exporter.RemoteService;
import cn.cloudwiz.dalian.snmp.exporter.SnmpExporter;
import cn.cloudwiz.dalian.snmp.exporter.smdb.SMDBRemoteService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ExporterTest {

    @Autowired
    private SnmpExporter exporter;
    @Autowired
    private RemoteService remotebs;
    @Autowired
    private ProjectionFactory proxyFactory;

    @Ignore
    @Test
    public void getMonitorItem() throws IOException {
        List<? extends MonitorDevice> devices = remotebs.getMonitorDevices();
        MonitorDevice device = devices.stream().filter(item-> Objects.equals(item.getAddress(), "192.168.1.66"))
                .findFirst().get();
        List<? extends MonitorItem> items = remotebs.getMonitorItem(device);
        items.forEach(item -> {
            System.out.println(JsonUtils.toJson(item));
        });
    }

    @Ignore
    @Test
    public void getMonitorDevices() throws IOException {
        List<? extends MonitorDevice> devices = remotebs.getMonitorDevices();
        devices.forEach(item -> {
            System.out.println(JsonUtils.toJson(item));
        });
    }

//    @Ignore
    @Test
    public void startExport() throws IOException {
        exporter.run();
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

}

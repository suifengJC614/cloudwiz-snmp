package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.snmp.api.nms.SnmpDevice;
import cn.cloudwiz.dalian.snmp.api.nms.SnmpDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultDeviceService implements SnmpDeviceService {

    @Override
    public void createMonitorDevice(SnmpDevice device) {

    }

    @Override
    public List<SnmpDevice> getMonitorDevice() {
        return null;
    }

}

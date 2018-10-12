package cn.cloudwiz.dalian.snmp.api.nms;

import java.util.List;

public interface SnmpDeviceService {

    public void createMonitorDevice(SnmpDevice device);

    public List<SnmpDevice> getMonitorDevice();

}

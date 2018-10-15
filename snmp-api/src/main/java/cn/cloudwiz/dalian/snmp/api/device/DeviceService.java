package cn.cloudwiz.dalian.snmp.api.device;

import java.util.List;

public interface DeviceService {

    public Long save(MonitorDevice device);

    public List<MonitorDevice> getMonitorDevice();

}

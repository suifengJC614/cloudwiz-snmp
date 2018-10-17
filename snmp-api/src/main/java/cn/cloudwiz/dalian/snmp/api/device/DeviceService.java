package cn.cloudwiz.dalian.snmp.api.device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceService {

    public Long save(MonitorDevice device);

    public void remove(Long key);

    public MonitorDevice getMonitorDevice(Long key);

    public Page<MonitorDevice> getMonitorDevices(DeviceParams params, Pageable pageable);

    public List<MonitorDevice> getMonitorDevices(Long brandKey);

}

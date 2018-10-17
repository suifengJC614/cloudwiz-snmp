package cn.cloudwiz.dalian.snmp.exporter;

import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RemoteService {

    public List<? extends MonitorDevice> getMonitorDevices() throws IOException;

    public List<? extends MonitorItem> getMonitorItem(MonitorDevice device) throws IOException;

    public void sendSnmpResult(MonitorDevice device, List<? extends MonitorItem> items, Map<String, String> result) throws IOException;

    public boolean isPause();

}

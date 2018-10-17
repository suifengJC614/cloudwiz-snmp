package cn.cloudwiz.dalian.snmp.exporter;

import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SnmpService {

    public Map<String, String> get(MonitorDevice device, List<String> oids) throws IOException;

    public Map<String, String> walk(MonitorDevice device, List<String> oids) throws IOException;

}

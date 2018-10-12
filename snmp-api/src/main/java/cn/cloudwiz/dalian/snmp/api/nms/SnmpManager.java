package cn.cloudwiz.dalian.snmp.api.nms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SnmpManager {

    public Map<String, String> get(SnmpDevice device, List<String> oids) throws IOException;

    public Map<String, String> walk(SnmpDevice device, List<String> oids) throws IOException;


}

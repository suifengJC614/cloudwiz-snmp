package cn.cloudwiz.dalian.snmp.api.nms;

import java.util.List;

public interface SnmpRequest {

    public String getAddress();

    public List<String> getOids();

}

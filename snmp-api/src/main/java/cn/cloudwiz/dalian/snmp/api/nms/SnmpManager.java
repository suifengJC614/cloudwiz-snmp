package cn.cloudwiz.dalian.snmp.api.nms;

import java.io.IOException;

public interface SnmpManager {

    public void get() throws IOException;

    public void walk() throws IOException;


}

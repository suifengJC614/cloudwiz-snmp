package cn.cloudwiz.dalian.snmp.api.nms;

import java.util.List;

public interface SnmpDevice {

    public String getDeviceClass();

    public String getDeviceName();

    public DeviceType getDeviceType();

    public String getModelNumber();

    public String getAddress();

    public Integer getPort();

    public SnmpVersion getVersion();

}

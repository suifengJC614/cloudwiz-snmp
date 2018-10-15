package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.snmp.api.device.CommunityDevice;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import org.springframework.beans.factory.annotation.Value;

public interface TestData {

    public DeviceBrand getDeviceBrand();

    public CommunityDevice getMonitorDevice();

    public MonitorItem getMonitorItem();

    public static interface NullKey {
        @Value("#{null}")
        public Long getKey();
    }

}

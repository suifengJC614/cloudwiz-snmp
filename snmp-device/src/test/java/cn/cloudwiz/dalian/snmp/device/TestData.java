package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import org.springframework.beans.factory.annotation.Value;

public interface TestData {

    public DeviceBrand getDeviceBrand();

    public static interface NullKey {
        @Value("#{null}")
        public Long getKey();
    }

}

package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.nms.SnmpDevice;
import cn.cloudwiz.dalian.snmp.api.nms.SnmpDeviceService;
import cn.cloudwiz.dalian.snmp.nms.config.NmsConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DefaultDeviceService implements SnmpDeviceService {

    @Autowired
    private NmsConfig config;

    @PostConstruct
    public void init() throws Exception {

    }

    @Override
    public void createMonitorDevice(SnmpDevice device) {
        System.out.println(JsonUtils.toJson(device));
    }

    @Override
    public List<SnmpDevice> getMonitorDevice() {
        return null;
    }

}

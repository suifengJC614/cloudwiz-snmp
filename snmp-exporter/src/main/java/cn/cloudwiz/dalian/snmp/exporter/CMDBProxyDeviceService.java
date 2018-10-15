package cn.cloudwiz.dalian.snmp.exporter;

import cn.cloudwiz.dalian.commons.utils.HttpClient;
import cn.cloudwiz.dalian.commons.utils.HttpException;
import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CMDBProxyDeviceService implements DeviceService {

    @Override
    public List<MonitorDevice> getMonitorDevice() {
        HttpClient client = HttpClient.getHttpClient();
        try {
            String response = client.get("https://aws-alert.cloudwiz.cn/node_manager/node/ip?token=a691e4af9a1c72ed8d8d1c940006f4a05298b964");
            System.out.println(response);
        } catch (HttpException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Long save(MonitorDevice device) {
        return null;
    }
}

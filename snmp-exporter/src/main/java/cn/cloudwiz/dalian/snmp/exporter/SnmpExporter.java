package cn.cloudwiz.dalian.snmp.exporter;

import cn.cloudwiz.dalian.commons.utils.HttpClient;
import cn.cloudwiz.dalian.commons.utils.HttpException;
import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class SnmpExporter implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpExporter.class);

    @Autowired
    private RemoteService remotebs;
    @Autowired
    private SnmpService snmpbs;
    @Value("${exporter.enable-auto-export}")
    private boolean enableExport;

    @Scheduled(cron = "${exporter.auto-export-cron}")
    public void autoExport() {
        if (remotebs.isPause()) {
            LOGGER.info("snmp export pause.");
            return;
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        LOGGER.info("start snmp export info.");
        try {
            long start = System.currentTimeMillis();
            List<? extends MonitorDevice> devices = remotebs.getMonitorDevices();
            devices.parallelStream().forEach(device -> {
                try {
                    List<? extends MonitorItem> items = remotebs.getMonitorItem(device);
                    if (CollectionUtils.isEmpty(items)) {
                        LOGGER.warn(String.format("export snmp for device[%s] cancel, reason: not found monitor items.",
                                device.getAddress()));
                        return;
                    }
                    List<String> oids = items.stream().map(MonitorItem::getOid).collect(Collectors.toList());
                    Map<String, String> result = new HashMap<>();
                    for (String oid : oids) {
                        Map<String, String> oidResult = snmpbs.get(device, Arrays.asList(oid));
                        if(MapUtils.isNotEmpty(oidResult)){
                            result.putAll(oidResult);
                        }
                    }
                    System.out.println(result);
                    if (MapUtils.isEmpty(result)) {
                        LOGGER.warn(String.format("export snmp for device[%s] ignore, reason: snmp result is empty",
                                device.getAddress()));
                        return;
                    }
                    Map<String, Number> sendResult = result.entrySet().parallelStream().filter(item -> {
                        try {
                            Double.parseDouble(item.getValue());
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }).collect(Collectors.toMap(
                            item -> item.getKey(),
                            item -> Double.parseDouble(item.getValue())
                    ));
                    remotebs.sendSnmpResult(device, items, sendResult);
                    LOGGER.info(String.format("export snmp for device[%s] success.", device.getAddress()));
                } catch (Throwable e) {
                    LOGGER.warn(String.format("export snmp for device[%s] failed, reason: %s", device.getAddress(), e.getMessage()));
                }
            });
            LOGGER.info(String.format("snmp export success, used time[%sms]", System.currentTimeMillis() - start));
        } catch (IOException e) {
            LOGGER.error(String.format("find monitor device failed, reason: %s", e.getMessage()), e);
        } catch (Throwable e) {
            LOGGER.error(String.format("export failed, reason: %S", e.getMessage()), e);
        }
    }

}

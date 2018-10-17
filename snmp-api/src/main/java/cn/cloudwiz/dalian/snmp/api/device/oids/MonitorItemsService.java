package cn.cloudwiz.dalian.snmp.api.device.oids;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MonitorItemsService {

    public Long save(MonitorItem item);

    public void remove(Long key);

    public MonitorItem getMonitorItem(Long key);

    public Page<MonitorItem> getMonitorItems(MonitorItemParams params, Pageable pageable);

    public List<MonitorItem> getMonitorItems(Long brandKey);

}

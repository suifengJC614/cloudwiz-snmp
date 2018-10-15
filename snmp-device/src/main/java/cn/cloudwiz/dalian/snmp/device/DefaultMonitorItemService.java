package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.core.BaseData;
import cn.cloudwiz.dalian.commons.core.exceptions.DataNotFoundException;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemsService;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceBrandDao;
import cn.cloudwiz.dalian.snmp.device.dao.MonitorItemDao;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import cn.cloudwiz.dalian.snmp.device.entity.MonitorItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class DefaultMonitorItemService implements MonitorItemsService {

    @Autowired
    private MonitorItemDao itemDao;
    @Autowired
    private DeviceBrandDao brandDao;

    @Override
    public Long save(MonitorItem item) {
        Assert.notNull(item, "save monitor item, item cannot be null");
        Long key = item.getKey();
        MonitorItemEntity entity = null;
        if (key != null) {
            entity = itemDao.getEntityByKey(key);
        }
        if (entity == null) {
            entity = BaseData.merge(MonitorItemEntity.class, item);
            entity.setDeviceBrand(item.getDeviceBrand());
            Assert.notNull(entity.getBrandKey(), "save monitor item, device brand cannot be null");
            BrandEntity brand = brandDao.getEntityByKey(entity.getBrandKey());
            if (brand == null) {
                throw new DataNotFoundException(String.format("save monitor item, device brand[%s] not found.",
                        entity.getBrandKey()));
            }
            itemDao.insert(entity);
        } else {
            entity.merge(item);
            itemDao.update(entity);
        }
        return entity.getPrimaryKey();
    }

}

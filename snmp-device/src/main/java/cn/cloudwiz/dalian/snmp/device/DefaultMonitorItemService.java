package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.core.BaseData;
import cn.cloudwiz.dalian.commons.core.exceptions.DataNotFoundException;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemParams;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemsService;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceBrandDao;
import cn.cloudwiz.dalian.snmp.device.dao.MonitorItemDao;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import cn.cloudwiz.dalian.snmp.device.entity.MonitorItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultMonitorItemService implements MonitorItemsService {

    @Autowired
    private MonitorItemDao itemDao;
    @Autowired
    private DeviceBrandDao brandDao;
    @Autowired
    private ProjectionFactory proxyFactory;

    @Override
    public List<MonitorItem> getMonitorItems(Long brandKey) {
        Assert.notNull(brandKey, "getMonitorItems by brand, brandKey cannot be null");
        List<MonitorItemEntity> result = itemDao.getListByBrand(brandKey);
        return result.stream().map(item -> projection(MonitorItem.class, item))
                .collect(Collectors.toList());
    }

    @Override
    public MonitorItem getMonitorItem(Long key) {
        Assert.notNull(key, "getMonitorItem, key cannot be null");
        MonitorItemEntity entity = itemDao.getEntityByKey(key);
        if (entity == null) {
            throw new DataNotFoundException(String.format("getMonitorItem, key[%s] is not found.", key));
        }
        return projection(MonitorItem.class, entity);
    }

    @Override
    public Page<MonitorItem> getMonitorItems(MonitorItemParams params, Pageable pageable) {
        Page<MonitorItemEntity> result = itemDao.getPageByParams(params, pageable);
        return result.map(item -> projection(MonitorItem.class, item));
    }

    protected <T extends MonitorItem> T projection(Class<T> type, MonitorItemEntity entity) {
        return proxyFactory.createNullableProjection(type, entity);
    }

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

    @Override
    public void remove(Long key) {
        if (key != null) {
            MonitorItemEntity entity = itemDao.getEntityByKey(key);
            if (entity != null) {
                itemDao.delete(key);
            }
        }
    }
}

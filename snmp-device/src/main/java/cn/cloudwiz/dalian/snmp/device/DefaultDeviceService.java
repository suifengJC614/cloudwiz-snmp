package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.core.BaseData;
import cn.cloudwiz.dalian.commons.core.exceptions.DataNotFoundException;
import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.*;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceBrandDao;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceDao;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import cn.cloudwiz.dalian.snmp.device.entity.DeviceEntity;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultDeviceService implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private DeviceBrandDao brandDao;

    @Override
    public List<MonitorDevice> getMonitorDevices(Long brandKey) {
        Assert.notNull(brandKey, "getMonitorDevice by brand, brandKey cannot be null");
        List<DeviceEntity> result = deviceDao.getListByBrand(brandKey);
        return result.stream().map(item -> projecion(item.getVersion().getDataType(), item))
                .collect(Collectors.toList());
    }

    @Override
    public MonitorDevice getMonitorDevice(Long key) {
        Assert.notNull(key, "getMonitorDevice, device key cannot be null");
        DeviceEntity entity = deviceDao.getEntityByKey(key);
        if (entity == null) {
            throw new DataNotFoundException(String.format("getMonitorDevice, device key[%s] is not found.", key));
        }
        SnmpVersion version = entity.getVersion();
        return projecion(version.getDataType(), entity);
    }

    @Override
    public Page<MonitorDevice> getMonitorDevices(DeviceParams params, Pageable pageable) {
        Page<DeviceEntity> result = deviceDao.getPageByParams(params, pageable);
        return result.map(item -> projecion(item.getVersion().getDataType(), item));
    }

    protected <T extends MonitorDevice> T projecion(Class<T> type, DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        Map<String, Object> extendDatas = new HashMap<>();
        if (entity.getProperties() != null) {
            extendDatas.putAll(entity.getProperties());
        }
        return proxyFactory.createProjection(type, Arrays.asList(entity, extendDatas));
    }

    @Override
    public Long save(MonitorDevice device) {
        Assert.notNull(device, "save monitor device, device cannot be null");
        Long key = device.getKey();
        DeviceEntity entity = null;
        if (key != null) {
            entity = deviceDao.getEntityByKey(key);
        }
        if (entity == null) {
            entity = BaseData.merge(DeviceEntity.class, device);
            mergeProperties(entity, device);
            entity.setDeviceBrand(device.getDeviceBrand());
            Assert.notNull(entity.getBrandKey(), "save monitor device, device brand cannot be null");
            BrandEntity brand = brandDao.getEntityByKey(entity.getBrandKey());
            if (brand == null) {
                throw new DataNotFoundException(String.format("save monitor device, device brand[%s] not found.",
                        entity.getBrandKey()));
            }
            deviceDao.insert(entity);
        } else {
            entity.merge(device);
            mergeProperties(entity, device);
            deviceDao.update(entity);
        }
        return entity.getPrimaryKey();
    }

    @Override
    public void remove(Long key) {
        if (key != null) {
            DeviceEntity entity = deviceDao.getEntityByKey(key);
            if (entity != null) {
                deviceDao.delete(key);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void mergeProperties(DeviceEntity entity, MonitorDevice device) {
        Map<String, Object> datas = JsonUtils.toBean(JsonUtils.toJson(device), Map.class);
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(MonitorDevice.class);
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getReadMethod() != null) {
                datas.remove(descriptor.getName());
            }
        }
        entity.setProperties(datas);
    }

}

package cn.cloudwiz.dalian.snmp.device;

import cn.cloudwiz.dalian.commons.core.BaseData;
import cn.cloudwiz.dalian.commons.core.exceptions.DataNotFoundException;
import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.SnmpVersion;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceBrandDao;
import cn.cloudwiz.dalian.snmp.device.dao.DeviceDao;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import cn.cloudwiz.dalian.snmp.device.entity.DeviceEntity;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

@Service
public class DefaultDeviceService implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    private DeviceBrandDao brandDao;

    @Override
    public List<MonitorDevice> getMonitorDevice() {
        return null;
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

package cn.cloudwiz.dalian.snmp.device.dao;

import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceClass;
import cn.cloudwiz.dalian.snmp.device.entity.BrandEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceBrandDao {

    public void insert(BrandEntity entity);

    public void update(BrandEntity entity);

    public void delete(Long key);

    public BrandEntity getEntityByKey(Long key);

    public List<BrandEntity> getListByClassify(@Param("classify") DeviceClass classify);

}

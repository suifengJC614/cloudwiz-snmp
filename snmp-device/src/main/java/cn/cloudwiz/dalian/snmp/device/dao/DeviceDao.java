package cn.cloudwiz.dalian.snmp.device.dao;

import cn.cloudwiz.dalian.snmp.device.entity.DeviceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface DeviceDao {

    public void insert(DeviceEntity entity);

    public void update(DeviceEntity entity);

    public void delete(Long key);

    public DeviceEntity getEntityByKey(Long key);


}

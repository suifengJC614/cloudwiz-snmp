package cn.cloudwiz.dalian.snmp.device.dao;

import cn.cloudwiz.dalian.snmp.device.entity.MonitorItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonitorItemDao {

    public void insert(MonitorItemEntity entity);

    public void update(MonitorItemEntity entity);

    public void delete(Long key);

    public MonitorItemEntity getEntityByKey(Long key);

    public List<MonitorItemEntity> getListByBrand(@Param("brandKey") Long brandKey);

}

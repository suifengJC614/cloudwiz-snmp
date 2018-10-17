package cn.cloudwiz.dalian.snmp.device.dao;

import cn.cloudwiz.dalian.commons.utils.PageFactory;
import cn.cloudwiz.dalian.snmp.api.device.DeviceParams;
import cn.cloudwiz.dalian.snmp.device.entity.DeviceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface DeviceDao {

    public void insert(DeviceEntity entity);

    public void update(DeviceEntity entity);

    public void delete(Long key);

    public DeviceEntity getEntityByKey(Long key);

    public List<DeviceEntity> getListByBrand(@Param("brandKey") Long brandKey);

    public List<DeviceEntity> getListByParams(@Param("params") DeviceParams params, @Param("page") Pageable page);

    public Long getCountByParams(@Param("params") DeviceParams params);

    default Page<DeviceEntity> getPageByParams(DeviceParams params, Pageable page){
        List<DeviceEntity> content = getListByParams(params, page);
        Long total = getCountByParams(params);
        return PageFactory.createPage(content, page, total);
    }

}

package cn.cloudwiz.dalian.snmp.device.dao;

import cn.cloudwiz.dalian.commons.utils.PageFactory;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemParams;
import cn.cloudwiz.dalian.snmp.device.entity.MonitorItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface MonitorItemDao {

    public void insert(MonitorItemEntity entity);

    public void update(MonitorItemEntity entity);

    public void delete(Long key);

    public MonitorItemEntity getEntityByKey(Long key);

    public List<MonitorItemEntity> getListByBrand(@Param("brandKey") Long brandKey);

    public List<MonitorItemEntity> getListByParams(@Param("params")MonitorItemParams params, @Param("page") Pageable page);

    public Long getCountByParams(@Param("params")MonitorItemParams params);

    default Page<MonitorItemEntity> getPageByParams(MonitorItemParams params, Pageable pageable){
        List<MonitorItemEntity> content = getListByParams(params, pageable);
        Long total = getCountByParams(params);
        return PageFactory.createPage(content, pageable, total);
    }

}

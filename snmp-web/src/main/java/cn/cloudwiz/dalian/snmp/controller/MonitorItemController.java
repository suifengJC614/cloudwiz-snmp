package cn.cloudwiz.dalian.snmp.controller;

import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemParams;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemsService;
import cn.cloudwiz.dalian.snmp.utils.ProjectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("oids")
@Api(description = "监控项相关接口服务")
public class MonitorItemController {

    @Autowired
    private MonitorItemsService itemsbs;
    @Autowired
    private ProjectionFactory proxyFactory;

    @ApiOperation("创建监控项信息")
    @ApiImplicitParam(name = "item", value = "要创建的监控项信息", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<Long> create(@RequestBody MonitorItem item){
        Long savedKey = itemsbs.save(proxyFactory.createProjection(NullKeyItem.class, item));
        return RestResponse.success(savedKey);
    }

    @ApiOperation("查询监控项信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exporterName", value = "exporter名称,模糊查询", paramType = "query"),
            @ApiImplicitParam(name = "brandKey", value = "品牌ID", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse<Page<MonitorItem>> find(MonitorItemParams params, Pageable pageable) {
        Page<MonitorItem> result = itemsbs.getMonitorItems(params, pageable);
        return RestResponse.success(result);
    }

    @ApiOperation("获取指定的监控项信息")
    @ApiImplicitParam(name = "key", value = "监控项ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.GET)
    public RestResponse<MonitorItem> get(@PathVariable("key") Long key) {
        MonitorItem result = itemsbs.getMonitorItem(key);
        return RestResponse.success(result);
    }

    @ApiOperation(value = "修改指定的监控项信息", notes = "body的json内容只需要放要修改的字段即可，其他的不变")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "监控项ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "item", value = "要修改的监控项信息", paramType = "body")
    })
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.PUT)
    public RestResponse<Void> update(@PathVariable("key") Long key, @RequestBody MonitorItem item){
        itemsbs.save(ProjectionUtils.mergeKey(MonitorItem.class, key, item, proxyFactory));
        return RestResponse.success();
    }

    @ApiOperation("删除指定的监控项信息")
    @ApiImplicitParam(name = "key", value = "监控项ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.DELETE)
    public RestResponse<Void> delete(@PathVariable("key") Long key) {
        itemsbs.remove(key);
        return RestResponse.success();
    }

    public interface NullKeyItem extends MonitorItem{
        @Value("#{null}")
        public Long getKey();
    }

}

package cn.cloudwiz.dalian.snmp.controller;

import cn.cloudwiz.dalian.commons.utils.JsonUtils;
import cn.cloudwiz.dalian.snmp.api.device.*;
import cn.cloudwiz.dalian.snmp.utils.ProjectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.TargetAware;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.nio.charset.Charset;

@RestController
@RequestMapping("device")
@Api(description = "监控设备相关接口文档")
public class MonitorDeviceController {

    @Autowired
    private DeviceService devicebs;
    @Autowired
    private ProjectionFactory proxyFactory;

    @ApiOperation("创建监控设备信息")
    @ApiImplicitParam(name = "device", value = "监控设备信息", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<Long> create(@RequestBody MonitorDevice device) {
        SnmpVersion version = device.getVersion();
        device = proxyFactory.createProjection(NullKeyDevice.class, ((TargetAware) device).getTarget());
        device = proxyFactory.createProjection(version.getDataType(), JsonUtils.fromJson(JsonUtils.toJson(device)));
        Long savedKey = devicebs.save(device);
        return RestResponse.success(1L);
    }

    @ApiOperation("查询监控设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exporterName", value = "exporter名称,模糊查询", paramType = "query"),
            @ApiImplicitParam(name = "deviceName", value = "device名称,模糊查询", paramType = "query"),
            @ApiImplicitParam(name = "brandKey", value = "品牌ID", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse<Page<MonitorDevice>> find(DeviceParams params, Pageable pageable) {
        Page<MonitorDevice> result = devicebs.getMonitorDevices(params, pageable);
        return RestResponse.success(result);
    }

    @ApiOperation("获取指定的监控设备信息")
    @ApiImplicitParam(name = "key", value = "监控设备ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.GET)
    public RestResponse<MonitorDevice> get(@PathVariable("key") Long key) {
        MonitorDevice result = devicebs.getMonitorDevice(key);
        return RestResponse.success(result);
    }

    @ApiOperation(value = "修改指定的监控设备信息", notes = "body的json内容只需要放要修改的字段即可，其他的不变")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "监控设备ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "device", value = "要修改的监控设备信息", paramType = "body")
    })
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.PUT)
    public RestResponse<Void> update(@PathVariable("key") Long key, @RequestBody MonitorDevice device){
        SnmpVersion version = device.getVersion();
        device = proxyFactory.createProjection(version.getDataType(), ((TargetAware) device).getTarget());
        devicebs.save(ProjectionUtils.mergeKey(version.getDataType(), key, device, proxyFactory));
        return RestResponse.success();
    }

    @ApiOperation("删除指定的监控设备信息")
    @ApiImplicitParam(name = "key", value = "监控设备ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.DELETE)
    public RestResponse<Void> delete(@PathVariable("key") Long key) {
        devicebs.remove(key);
        return RestResponse.success();
    }

    public interface NullKeyDevice extends CommunityDevice, SecurityDevice {
        @Value("#{null}")
        public Long getKey();
        @Value("#{target.version}")
        public SnmpVersion getVersion();
    }

}

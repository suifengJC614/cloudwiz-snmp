package cn.cloudwiz.dalian.snmp.controller;

import cn.cloudwiz.dalian.snmp.api.device.DeviceService;
import cn.cloudwiz.dalian.snmp.api.device.MonitorDevice;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrandService;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceClass;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItem;
import cn.cloudwiz.dalian.snmp.api.device.oids.MonitorItemsService;
import cn.cloudwiz.dalian.snmp.utils.ProjectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("brand")
@Api(description = "设备品牌相关接口")
public class DeviceBrandController {

    @Autowired
    private DeviceBrandService brandbs;
    @Autowired
    private DeviceService devicebs;
    @Autowired
    private MonitorItemsService itemsbs;
    @Autowired
    private ProjectionFactory proxyFactory;

    @ApiOperation("上传品牌LOGO")
    @RequestMapping(path = "logo", method = RequestMethod.POST)
    public RestResponse<String> uploadLogo(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(filename, ".");
        String result = brandbs.uploadLogo(file.getInputStream(), suffix);
        return RestResponse.success(result);
    }

    @ApiOperation("创建设备品牌信息")
    @ApiImplicitParam(name = "brand", value = "设备品牌信息", paramType = "body")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<Long> create(@RequestBody DeviceBrand brand) throws IOException {
        Long savedkey = brandbs.save(proxyFactory.createProjection(NullKeyBrand.class, brand));
        return RestResponse.success(savedkey);
    }

    @ApiOperation("查询设备品牌信息")
    @ApiImplicitParam(name = "classify", value = "品牌分类", paramType = "query", allowableValues = "NETWORK,FIREWALL,SERVER,STORAGE,UPS")
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse<List<DeviceBrand>> find(DeviceClass classify) {
        List<DeviceBrand> result = brandbs.getDeviceBrands(classify);
        return RestResponse.success(result);
    }

    @ApiOperation("获取指定的设备品牌信息")
    @ApiImplicitParam(name = "key", value = "品牌ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.GET)
    public RestResponse<DeviceBrand> get(@PathVariable("key") Long key) {
        DeviceBrand result = brandbs.getDeviceBrand(key);
        return RestResponse.success(result);
    }

    @ApiOperation(value = "修改指定的设备品牌信息", notes = "body的json内容只需要放要修改的字段即可，其他的不变")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "品牌ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "brand", value = "要修改的设备品牌信息", paramType = "body")
    })
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.PUT)
    public RestResponse<Void> update(@PathVariable("key") Long key, @RequestBody DeviceBrand brand) throws IOException {
        brandbs.save(ProjectionUtils.mergeKey(DeviceBrand.class, key, brand, proxyFactory));
        return RestResponse.success();
    }

    @ApiOperation("删除指定的设备品牌信息")
    @ApiImplicitParam(name = "key", value = "品牌ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}", method = RequestMethod.DELETE)
    public RestResponse<Void> delete(@PathVariable("key") Long key) {
        brandbs.remove(key);
        return RestResponse.success();
    }

    @ApiOperation("获取指定的设备品牌下的监控设备")
    @ApiImplicitParam(name = "key", value = "品牌ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}/devices", method = RequestMethod.GET)
    public RestResponse<List<MonitorDevice>> findMonitorDevice(@PathVariable("key") Long key) {
        List<MonitorDevice> result = devicebs.getMonitorDevices(key);
        return RestResponse.success(result);
    }

    @ApiOperation("获取指定的设备品牌下的监控项")
    @ApiImplicitParam(name = "key", value = "品牌ID", paramType = "path", required = true)
    @RequestMapping(path = "{key:\\d+}/monitor_items", method = RequestMethod.GET)
    public RestResponse<List<MonitorItem>> findMonitorItems(@PathVariable("key") Long key) {
        List<MonitorItem> result = itemsbs.getMonitorItems(key);
        return RestResponse.success(result);
    }

    public interface NullKeyBrand extends DeviceBrand {
        @Value("#{null}")
        public Long getKey();
    }
}

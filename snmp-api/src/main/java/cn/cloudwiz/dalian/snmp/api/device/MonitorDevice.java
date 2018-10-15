package cn.cloudwiz.dalian.snmp.api.device;

import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ProjectionBody
@ApiModel(description = "监控设备")
public interface MonitorDevice {

    @ApiModelProperty("主键")
    public Long getKey();

    @ApiModelProperty("品牌")
    public DeviceBrand getDeviceBrand();

    @ApiModelProperty("执行监控的实例名称")
    public String getExporterName();

    @ApiModelProperty("设备名称")
    public String getDeviceName();

    @ApiModelProperty("设备类型")
    public DeviceType getDeviceType();

    @ApiModelProperty("设备型号")
    public String getModel();

    @ApiModelProperty("监控地址")
    public String getAddress();

    @ApiModelProperty("监控端口号")
    public Integer getPort();

    @ApiModelProperty("SNMP版本")
    public SnmpVersion getVersion();

}

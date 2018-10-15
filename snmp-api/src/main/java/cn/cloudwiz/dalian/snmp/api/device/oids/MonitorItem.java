package cn.cloudwiz.dalian.snmp.api.device.oids;

import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ProjectionBody
@ApiModel(description = "监控项")
public interface MonitorItem {

    @ApiModelProperty("主键")
    public Long getKey();

    @ApiModelProperty("品牌")
    public DeviceBrand getDeviceBrand();

    @ApiModelProperty("执行监控的实例名称")
    public String getExporterName();

    @ApiModelProperty("SNMP监控的OID")
    public String getOid();

    @ApiModelProperty("监控值保存的键名")
    public String getSaveKey();

    @ApiModelProperty("监控值的单位")
    public String getValueUnit();

    @ApiModelProperty("监控值的类型")
    public ValueType getValueType();

}

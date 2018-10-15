package cn.cloudwiz.dalian.snmp.api.device;

import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ProjectionBody
@ApiModel(description = "SNMPv1、v2监控设备")
public interface CommunityDevice extends MonitorDevice {

    @ApiModelProperty("SNMP社区名称")
    public String getCommunity();

}

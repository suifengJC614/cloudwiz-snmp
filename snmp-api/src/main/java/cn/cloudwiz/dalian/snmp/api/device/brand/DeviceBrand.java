package cn.cloudwiz.dalian.snmp.api.device.brand;

import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ProjectionBody
@ApiModel(description = "设备品牌")
public interface DeviceBrand {

    @ApiModelProperty("主键")
    public Long getKey();

    @ApiModelProperty("品牌名称")
    public String getBrandName();

    @ApiModelProperty("描述")
    public String getDescription();

    @ApiModelProperty("LOGO图片")
    public String getLogoImage();

    @ApiModelProperty("分类")
    public Set<DeviceClass> getClassifications();

    @ApiModelProperty("是否禁用")
    public boolean isDisable();

}

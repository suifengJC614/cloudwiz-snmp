package cn.cloudwiz.dalian.snmp.api.device;

import cn.cloudwiz.dalian.commons.projection.web.ProjectionBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ProjectionBody
@ApiModel(description = "SNMPv3监控设备")
public interface SecurityDevice extends MonitorDevice {

    default SnmpVersion getVersion(){
        return SnmpVersion.VERSION_3;
    }

    @ApiModelProperty("安全名称")
    public String getSecurityName();

    @ApiModelProperty("认证算法")
    public AuthType getAuthType();

    @ApiModelProperty("认证密钥")
    public String getAuthPassphrase();

    @ApiModelProperty("加密算法")
    public PrivacyType getPrivacyType();

    @ApiModelProperty("加密密钥")
    public String getPrivacyPassphrase();

}


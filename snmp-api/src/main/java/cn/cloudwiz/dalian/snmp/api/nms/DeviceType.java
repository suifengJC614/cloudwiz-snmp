package cn.cloudwiz.dalian.snmp.api.nms;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum DeviceType implements EnumItem<String> {

    CORE_ROUTER("核心路由器"),
    CORE_SWITCH("核心交换机"),
    ACCESS_SWITCH("接入交换机"),
    LAYER_2_SWITCH("二层交换机");

    private String label;

    DeviceType(String label) {
        this.label = label;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getLabel() {
        return label;
    }
}

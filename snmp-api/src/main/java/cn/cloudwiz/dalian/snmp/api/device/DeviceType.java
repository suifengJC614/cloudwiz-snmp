package cn.cloudwiz.dalian.snmp.api.device;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum DeviceType implements EnumItem<String>, EnumType<Integer> {

    CORE_ROUTER(1, "核心路由器"),
    CORE_SWITCH(2, "核心交换机"),
    ACCESS_SWITCH(3, "接入交换机"),
    LAYER2_SWITCH(4, "二层交换机");

    private int code;
    private String label;

    DeviceType(int code, String label) {
        this.code = code;
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

    @Override
    public Integer getJdbcValue() {
        return code;
    }
}

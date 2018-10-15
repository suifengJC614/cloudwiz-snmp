package cn.cloudwiz.dalian.snmp.api.device.oids;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum ValueType implements EnumItem<String>, EnumType<Integer> {

    NUMBER(1, "数值"),
    TEXT(2, "文本");

    private int code;
    private String label;

    ValueType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getJdbcValue() {
        return code;
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

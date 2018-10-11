package cn.cloudwiz.dalian.snmp.api.nms;

import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum AuthType implements EnumItem<String> {

    MD5;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getLabel() {
        return name();
    }
}

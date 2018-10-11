package cn.cloudwiz.dalian.snmp.api.nms;

import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum PrivacyType implements EnumItem<String> {

    DES;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getLabel() {
        return name();
    }
}

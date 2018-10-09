package cn.cloudwiz.dalian.snmp.api.nms;

import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum SnmpVersion implements EnumItem<String> {

    VERSION_1("SNMPv1"),
    VERSION_2C("SNMPv2"),
    VERSION_3("SNMPv3");

    private String label;

    SnmpVersion(String label) {
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

package cn.cloudwiz.dalian.snmp.api.device;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum SnmpVersion implements EnumItem<String>, EnumType<Integer> {

    VERSION_1(1, "SNMPv1", CommunityDevice.class),
    VERSION_2C(2, "SNMPv2", CommunityDevice.class),
    VERSION_3(3, "SNMPv3", SecurityDevice.class);

    private int code;
    private String label;
    private Class<? extends MonitorDevice> dataType;

    SnmpVersion(int code, String label, Class<? extends MonitorDevice> dataType) {
        this.code = code;
        this.label = label;
        this.dataType = dataType;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getLabel() {
        return label;
    }

    public Class<? extends MonitorDevice> getDataType() {
        return dataType;
    }

    @Override
    public Integer getJdbcValue() {
        return code;
    }
}

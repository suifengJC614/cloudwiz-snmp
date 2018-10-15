package cn.cloudwiz.dalian.snmp.api.device.brand;

import cn.cloudwiz.dalian.commons.core.orm.mybatis.EnumType;
import cn.cloudwiz.dalian.snmp.api.EnumItem;

public enum DeviceClass implements EnumItem<String>, EnumType<Integer> {

    NETWORK(1 << 0, "网络设备"),
    FIREWALL(1 << 1, "防火墙"),
    SERVER(1 << 2, "服务器"),
    STORAGE(1 << 3, "存储"),
    UPS(1 << 4, "UPS");

    private int code;
    private String label;

    DeviceClass(int code, String label) {
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

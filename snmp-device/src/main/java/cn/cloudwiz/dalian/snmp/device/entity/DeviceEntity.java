package cn.cloudwiz.dalian.snmp.device.entity;

import cn.cloudwiz.dalian.commons.core.orm.TimeDeleteEntity;
import cn.cloudwiz.dalian.snmp.api.device.DeviceType;
import cn.cloudwiz.dalian.snmp.api.device.SnmpVersion;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;

import java.util.Map;

public class DeviceEntity extends TimeDeleteEntity {

    private static final long serialVersionUID = 9122820615217102444L;

    private BrandEntity deviceBrand;
    private String exporterName;
    private String deviceName;
    private String model;
    private DeviceType deviceType;
    private String address;
    private Integer port;
    private SnmpVersion version;
    private Map<String, Object> properties;

    public Long getBrandKey() {
        if (deviceBrand != null) {
            return deviceBrand.getPrimaryKey();
        }
        return null;
    }

    public void setBrandKey(Long brandKey) {
        if (deviceBrand == null) {
            deviceBrand = new BrandEntity();
        }
        deviceBrand.setPrimaryKey(brandKey);
    }

    public BrandEntity getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(DeviceBrand deviceBrand) {
        setBrandKey(deviceBrand == null ? null : deviceBrand.getKey());
    }

    public void setDeviceBrand(BrandEntity deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public SnmpVersion getVersion() {
        return version;
    }

    public void setVersion(SnmpVersion version) {
        this.version = version;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

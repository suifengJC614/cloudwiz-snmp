package cn.cloudwiz.dalian.snmp.device.entity;

import cn.cloudwiz.dalian.commons.core.orm.TimeDeleteEntity;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceBrand;
import cn.cloudwiz.dalian.snmp.api.device.oids.ValueType;

public class MonitorItemEntity extends TimeDeleteEntity {

    private static final long serialVersionUID = -923478206823125674L;

    private BrandEntity deviceBrand;
    private String exporterName;
    private String oid;
    private String saveKey;
    private String valueUnit;
    private ValueType valueType;

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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSaveKey() {
        return saveKey;
    }

    public void setSaveKey(String saveKey) {
        this.saveKey = saveKey;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }
}

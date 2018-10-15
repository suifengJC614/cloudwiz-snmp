package cn.cloudwiz.dalian.snmp.device.entity;

import cn.cloudwiz.dalian.commons.core.orm.TimeDeleteEntity;
import cn.cloudwiz.dalian.snmp.api.device.brand.DeviceClass;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Set;

public class BrandEntity extends TimeDeleteEntity {

    private static final long serialVersionUID = -6565888493009605057L;

    private String brandName;
    private String description;
    private String logoImage;
    private Boolean disable;
    private Set<DeviceClass> classifications;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public Set<DeviceClass> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<DeviceClass> classifications) {
        this.classifications = classifications;
    }

    public boolean isDisable() {
        return BooleanUtils.isTrue(disable);
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}

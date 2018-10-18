package cn.cloudwiz.dalian.snmp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("remote-config")
public class RemoteConfig {

    private String devicePath;
    private String itemsPath;
    private String tsdbPath;
    private TokenConfig token;
    private Long orgKey;
    private Long sysKey;

    public String getItemsPath() {
        return itemsPath;
    }

    public void setItemsPath(String itemsPath) {
        this.itemsPath = itemsPath;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public void setDevicePath(String devicePath) {
        this.devicePath = devicePath;
    }

    public String getTsdbPath() {
        return tsdbPath;
    }

    public void setTsdbPath(String tsdbPath) {
        this.tsdbPath = tsdbPath;
    }

    public TokenConfig getToken() {
        return token;
    }

    public void setToken(TokenConfig token) {
        this.token = token;
    }

    public Long getOrgKey() {
        return orgKey;
    }

    public void setOrgKey(Long orgKey) {
        this.orgKey = orgKey;
    }

    public Long getSysKey() {
        return sysKey;
    }

    public void setSysKey(Long sysKey) {
        this.sysKey = sysKey;
    }
}

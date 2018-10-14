package cn.cloudwiz.dalian.snmp.nms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@ConfigurationProperties(prefix="nms")
public class NmsConfig {

    private String cmdbPath;
    private String tsdbPath;
    private TokenConfig token;

    public String getCmdbPath() {
        return cmdbPath;
    }

    public void setCmdbPath(String cmdbPath) {
        this.cmdbPath = cmdbPath;
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
}

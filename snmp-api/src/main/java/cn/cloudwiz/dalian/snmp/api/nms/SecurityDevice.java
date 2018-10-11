package cn.cloudwiz.dalian.snmp.api.nms;

public interface SecurityDevice extends SnmpDevice {

    default SnmpVersion getVersion(){
        return SnmpVersion.VERSION_3;
    }

    public String getSecurityName();

    public AuthType getAuthType();

    public String getAuthPassphrase();

    public PrivacyType getPrivacyType();

    public String getPrivacyPassphrase();

}


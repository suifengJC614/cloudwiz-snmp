package cn.cloudwiz.dalian.snmp.config;

public class TokenConfig {

    private String username = "CloudInsight";
    private String password = "Cloud";
    private String url = "jdbc:mysql://123.206.91.96:3308/grafana";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

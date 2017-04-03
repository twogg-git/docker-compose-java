package emailboot.util;

import java.io.Serializable;

public class SMTPProperties  implements Serializable {

    private String host;
    private String port;
    private String username;
    private String password;
    private String protocol;
    private String smtpAuth;
    private String enableTls;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getEnableTls() {
        return enableTls;
    }

    public void setEnableTls(String enableTls) {
        this.enableTls = enableTls;
    }
}

package com.rsschool.sps.service.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.token")
public final class AuthProperties {

    private String prefix;
    private Integer expiration;
    private Integer refreshExpiration;
    private String secret;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(Integer refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}

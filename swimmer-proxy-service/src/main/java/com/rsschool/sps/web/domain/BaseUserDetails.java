package com.rsschool.sps.web.domain;

import com.rsschool.sps.domain.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class BaseUserDetails extends AbstractAuthenticationToken {

    private User principal;

    public BaseUserDetails(User user) {
        super(null);
        this.principal = user;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

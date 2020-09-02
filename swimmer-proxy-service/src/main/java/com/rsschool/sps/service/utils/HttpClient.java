package com.rsschool.sps.service.utils;

import com.rsschool.sps.domain.AccessToken;
import com.rsschool.sps.domain.CustomerWrapper;
import com.rsschool.sps.service.domain.AdminProperties;
import com.rsschool.sps.service.domain.AdminUser;
import kong.unirest.Config;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import org.springframework.stereotype.Component;

@Component
public class HttpClient {

    private static final String TOKEN_HEADER_NAME = "X-Alfacrm-Token";

    private final UnirestInstance client;
    private final String serverUrl;
    private final AdminUser adminUser;

    public HttpClient(AdminProperties adminProperties) {
        Config config = new Config();
        config.addDefaultHeader("Content-Type", "application/json");
        config.addDefaultHeader("Accept", "application/json");

        this.client = new UnirestInstance(config);
        this.serverUrl = adminProperties.getUrl();
        this.adminUser = new AdminUser(adminProperties.getEmail(), adminProperties.getApiKey());
    }

    public AccessToken login() {
        HttpResponse<AccessToken> response = client.post(buildUrl("/v2api/auth/login"))
                .body(adminUser)
                .asObject(AccessToken.class);
        if (!response.isSuccess()) {
            throw new RuntimeException("Server is temporary unavailable. Please try again later");
        }
        return response.getBody();
    }

    public CustomerWrapper findCustomers(String token) {
        HttpResponse<CustomerWrapper> response = client.post(buildUrl("/v2api/2/customer/index"))
                .header(TOKEN_HEADER_NAME, token)
                .body(adminUser)
                .asObject(CustomerWrapper.class);
        if (!response.isSuccess()) {
            throw new RuntimeException("Server is temporary unavailable. Please try again later");
        }
        return response.getBody();
    }

    private String buildUrl(String path) {
        return serverUrl + path;
    }

    public static String getTokenHeaderName() {
        return TOKEN_HEADER_NAME;
    }
}

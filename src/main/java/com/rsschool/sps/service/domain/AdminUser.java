package com.rsschool.sps.service.domain;

import com.google.gson.annotations.SerializedName;

public class AdminUser {

    private String email;

    @SerializedName("api_key")
    private String apiKey;

    public AdminUser() {
    }

    public AdminUser(String email, String apiKey) {
        this.email = email;
        this.apiKey = apiKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

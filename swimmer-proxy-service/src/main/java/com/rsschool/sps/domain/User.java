package com.rsschool.sps.domain;

public class User {

    private Long id;
    private String token;
    private String phoneNumber;

    public User() {
    }

    public User(Long id, String token, String phoneNumber) {
        this.id = id;
        this.token = token;
        this.phoneNumber = phoneNumber;
    }

    public User(Long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

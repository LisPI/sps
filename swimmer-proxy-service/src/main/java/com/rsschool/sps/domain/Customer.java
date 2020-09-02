package com.rsschool.sps.domain;

import java.util.Set;

public class Customer {

    private Long id;
    private Set<String> phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getPhone() {
        return phone;
    }

    public void setPhone(Set<String> phone) {
        this.phone = phone;
    }
}

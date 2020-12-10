package com.rsschool.sps.service.impl;

import com.rsschool.sps.domain.*;
import com.rsschool.sps.service.AuthService;
import com.rsschool.sps.service.JwtService;
import com.rsschool.sps.service.domain.AuthProperties;
import com.rsschool.sps.service.utils.HttpClient;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthProperties authProperties;
    private final HttpClient httpClient;

    public AuthServiceImpl(JwtService jwtService, AuthProperties authProperties, HttpClient httpClient) {
        this.jwtService = jwtService;
        this.authProperties = authProperties;
        this.httpClient = httpClient;
    }

    @Override
    public AuthToken login(Credentials credentials) {
        AuthToken authToken;
        try {
            AccessToken accessToken = httpClient.login();
            CustomerWrapper customerWrapper = httpClient.findCustomers(accessToken.getToken());
            Long customerId = fetchCustomer(customerWrapper.getItems(), credentials.getPhoneNumber())
                    .orElseThrow(() -> new BadCredentialsException("Bad credentials"))
                    .getId();

            User user = new User(customerId, accessToken.getToken(), credentials.getPhoneNumber());
            String jwtAuthToken = jwtService.generateAuthToken(user);
            String jwtRefreshToken = jwtService.generateRefreshToken(user);
            authToken = new AuthToken(customerId, authProperties.getPrefix(), jwtAuthToken, jwtRefreshToken, authProperties.getExpiration());
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }
        return authToken;
    }

    private Optional<Customer> fetchCustomer(Collection<Customer> customers, String phoneNumber) {
        return customers.stream()
                .filter(customer -> customer.getPhone().contains(phoneNumber))
                .findFirst();
    }

    @Override
    public AuthToken refresh(RefreshToken refreshToken) {
        AuthToken authToken;
        try {
            User user = jwtService.parseRefreshToken(refreshToken.getRefreshToken());
            AccessToken accessToken = httpClient.login();
            user.setToken(accessToken.getToken());
            String jwtAuthToken = jwtService.generateAuthToken(user);
            String jwtRefreshToken = jwtService.generateRefreshToken(user);
            authToken = new AuthToken(user.getId(), authProperties.getPrefix(), jwtAuthToken, jwtRefreshToken, authProperties.getExpiration());
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }
        return authToken;
    }
}

package com.rsschool.sps.service;

import com.rsschool.sps.domain.User;

public interface JwtService {

    String generateAuthToken(User user);

    User parseAuthToken(String token);

    String generateRefreshToken(User user);

    User parseRefreshToken(String token);

}

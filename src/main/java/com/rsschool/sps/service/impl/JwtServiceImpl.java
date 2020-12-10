package com.rsschool.sps.service.impl;

import com.rsschool.sps.domain.User;
import com.rsschool.sps.service.JwtService;
import com.rsschool.sps.service.domain.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class JwtServiceImpl implements JwtService {

    private static final String ERR_MSG_MALFORMED_TOKEN = "Authentication token is malformed";

    private final AuthProperties authProperties;

    public JwtServiceImpl(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public String generateAuthToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("token", user.getToken());
        return buildToken(claims, authProperties.getExpiration());
    }

    public User parseAuthToken(String token) {
        Claims body = parseToken(token);
        Long id = Long.parseLong(body.getSubject());
        String phoneNumber = (String) body.get("phoneNumber");
        String apiToken = (String) body.get("token");
        return new User(id, apiToken, phoneNumber);
    }

    public String generateRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getId()));
        claims.put("phoneNumber", user.getPhoneNumber());
        return buildToken(claims, authProperties.getRefreshExpiration());
    }

    public User parseRefreshToken(String token) {
        Claims body = parseToken(token);
        Long id = Long.parseLong(body.getSubject());
        String phoneNumber = (String) body.get("phoneNumber");
        return new User(id, phoneNumber);
    }

    private String buildToken(Claims claims, Integer exp) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, exp);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, authProperties.getSecret())
                .setExpiration(calendar.getTime())
                .compact();
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(authProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new BadCredentialsException(ERR_MSG_MALFORMED_TOKEN);
        }
    }
}

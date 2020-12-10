package com.rsschool.sps.web.security;

import com.rsschool.sps.domain.User;
import com.rsschool.sps.service.JwtService;
import com.rsschool.sps.service.domain.AuthProperties;
import com.rsschool.sps.web.domain.BaseUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String prefix;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthProperties authProperties, JwtService jwtService) {
        this.prefix = String.format("%s ", authProperties.getPrefix());
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(prefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            User user = extractAndDecodeJwt(request);

            if (user == null) {
                return;
            }

            Authentication auth = buildAuthenticationFromJwt(user, request);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new BadCredentialsException("Jwt not valid");
        }

        SecurityContextHolder.clearContext();
    }

    private User extractAndDecodeJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = authHeader.substring(prefix.length());
        return jwtService.parseAuthToken(token);
    }

    private Authentication buildAuthenticationFromJwt(User user, HttpServletRequest request) {
        BaseUserDetails userDetails = new BaseUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}

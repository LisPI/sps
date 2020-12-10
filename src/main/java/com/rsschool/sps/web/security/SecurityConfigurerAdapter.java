package com.rsschool.sps.web.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsFilter corsFilter;

    public SecurityConfigurerAdapter(JwtAuthenticationFilter jwtAuthenticationFilter, CorsFilter corsFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsFilter = corsFilter;
    }

    private static final String[] AUTHENTICATED_API_PATTERNS = {
            "/**"
    };

    private static final String[] PUBLIC_API_PATTERNS = {
            "/v2api/auth/**"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .authorizeRequests()
                .antMatchers(PUBLIC_API_PATTERNS).permitAll()
                .antMatchers(AUTHENTICATED_API_PATTERNS).authenticated();
    }

}

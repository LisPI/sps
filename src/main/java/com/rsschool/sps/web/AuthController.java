package com.rsschool.sps.web;

import com.rsschool.sps.domain.AuthToken;
import com.rsschool.sps.domain.Credentials;
import com.rsschool.sps.domain.RefreshToken;
import com.rsschool.sps.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v2api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public AuthToken login(@RequestBody Credentials credentials) {
        return authService.login(credentials);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refresh")
    public AuthToken refresh(@RequestBody RefreshToken refreshToken) {
        return authService.refresh(refreshToken);
    }

}

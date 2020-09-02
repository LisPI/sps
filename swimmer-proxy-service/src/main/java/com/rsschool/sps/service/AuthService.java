package com.rsschool.sps.service;

import com.rsschool.sps.domain.AuthToken;
import com.rsschool.sps.domain.Credentials;
import com.rsschool.sps.domain.RefreshToken;

public interface AuthService {

    AuthToken login(Credentials credentials);

    AuthToken refresh(RefreshToken refreshToken);

}

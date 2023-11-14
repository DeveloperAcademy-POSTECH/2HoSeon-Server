package com.twohoseon.app.service.refreshToken;

import com.twohoseon.app.dto.response.JWTToken;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshTokenService
 * @date : 10/16/23 3:58 PM
 * @modifyed : $
 **/
public interface RefreshTokenService {

    void saveRefreshToken(String refreshToken, String identifier, long expirationTime);

    JWTToken renewToken(String refreshToken, String identifier);

}

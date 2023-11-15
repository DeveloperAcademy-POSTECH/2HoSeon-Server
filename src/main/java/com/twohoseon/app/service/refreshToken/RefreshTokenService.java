package com.twohoseon.app.service.refreshToken;

import com.twohoseon.app.dto.response.TokenDTO;

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

    TokenDTO renewToken(String refreshToken);

    void saveRefreshTokenFromTokenDTO(TokenDTO tokenDTO, String providerId);
}

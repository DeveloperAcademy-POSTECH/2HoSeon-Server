package com.twohoseon.app.service.refreshToken;

import com.twohoseon.app.dto.TokenDTO;
import com.twohoseon.app.entity.RefreshToken;
import com.twohoseon.app.repository.member.RefreshTokenRepository;
import com.twohoseon.app.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshTokenServiceImpl
 * @date : 10/16/23 3:59 PM
 * @modifyed : $
 **/
@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void saveRefreshToken(String refreshToken, String identifier, long expirationTime) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .OAuthId(identifier)
                .refreshToken(refreshToken)
                .expirationTime(expirationTime)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public TokenDTO renewToken(String refreshToken, String identifier) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByOAuthIdAndRefreshToken(identifier, refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token이 존재하지 않습니다."));
        refreshTokenRepository.delete(refreshTokenEntity);
        return jwtTokenProvider.createAllToken(identifier);
    }

}

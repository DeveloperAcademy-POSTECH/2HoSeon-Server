package com.twohoseon.app.service.refreshToken;

import com.twohoseon.app.dto.response.JWTToken;
import com.twohoseon.app.entity.member.RefreshToken;
import com.twohoseon.app.exception.InvalidRefreshTokenException;
import com.twohoseon.app.repository.member.RefreshTokenRepository;
import com.twohoseon.app.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

                .refreshToken(refreshToken)
//                .expirationTime(expirationTime)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public JWTToken renewToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh Token does not exist."));

        refreshTokenEntity.banRefreshToken();
        refreshTokenRepository.save(refreshTokenEntity);
        JWTToken newToken = jwtTokenProvider.createAllToken(refreshTokenEntity.getProviderId());
        saveRefreshTokenFromTokenDTO(newToken, refreshTokenEntity.getProviderId());
        return newToken;
    }

    @Override
    public void saveRefreshTokenFromTokenDTO(JWTToken tokenDTO, String providerId) {
        refreshTokenRepository.save(RefreshToken.builder()
                .accessToken(tokenDTO.getAccessToken())
                .accessTokenExpirationTime(convertUnixTimestampToLocalDateTime(tokenDTO.getAccessExpirationTime()))
                .refreshToken(tokenDTO.getRefreshToken())
                .refreshTokenExpirationTime(convertUnixTimestampToLocalDateTime(tokenDTO.getRefreshExpirationTime()))
                .providerId(providerId)
                .build());
    }


    private static LocalDateTime convertUnixTimestampToLocalDateTime(long unixTimestamp) {
        Instant instant = Instant.ofEpochMilli(unixTimestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}

package com.twohoseon.app.util;

import com.twohoseon.app.dto.response.JWTToken;
import com.twohoseon.app.entity.member.RefreshToken;
import com.twohoseon.app.repository.member.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : JwtTokenProvider
 * @date : 2023/10/07 3:50 PM
 * @modifyed : $
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.expiration.access-token}")
    private int jwtExpirationMs;
    @Value("${jwt.expiration.refresh-token}")
    private int jwtRefreshExpirationMs;

    public JWTToken createAllToken(String username) {
        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + jwtExpirationMs);
        Date refreshExpiration = new Date(now.getTime() + jwtRefreshExpirationMs);

        Claims claims = Jwts.claims().setSubject("providerId");
        claims.put("type", "access");
        String accessToken = Jwts.builder()
                .setHeaderParam("providerId", username)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        claims = Jwts.claims().setSubject("providerId");
        claims.put("type", "refresh");
        String refreshToken = Jwts.builder()
                .setHeaderParam("providerId", username)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .providerId(username)
                .expirationTime(refreshExpiration.getTime())
                .build());
        return JWTToken.builder()
                .accessToken(accessToken)
                .accessExpirationTime(accessExpiration.getTime())
                .refreshToken(refreshToken)
                .refreshExpirationTime(refreshExpiration.getTime())
                .build();

    }

    public String getProviderIdFromToken(String token) {
        JwsHeader<?> header = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getHeader();
        return (String) header.get("providerId");
    }

    public boolean tokenValidation(String token, boolean isAccessToken) throws RuntimeException {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String type = claims.get("type", String.class);
        return type != null && type.equals(isAccessToken ? "access" : "refresh");
    }

    public String getHeaderToken(HttpServletRequest request, String type) {
        log.info("getHeaderToken", request.getHeader("Authorization"));
        log.info(request.getHeader("Authorization"));
        String result = type.equals("Access") ? request.getHeader("Authorization") : request.getHeader("Refresh_Token");
        if (result != null)
            return result.replace("Bearer ", "");
        else
            return null;
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }
}


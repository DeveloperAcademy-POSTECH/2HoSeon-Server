package com.twohoseon.app.util;

import com.twohoseon.app.dto.TokenDTO;
import com.twohoseon.app.entity.RefreshToken;
import com.twohoseon.app.repository.member.RefreshTokenRepository;
import io.jsonwebtoken.*;
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

    public TokenDTO createAllToken(String username) {
        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + jwtExpirationMs);
        Date refreshExpiration = new Date(now.getTime() + jwtRefreshExpirationMs);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("type", "access");
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        claims = Jwts.claims().setSubject(username);
        claims.put("type", "refresh");
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .OAuthId(username)
                .expirationTime(refreshExpiration.getTime())
                .build());
        );
        return TokenDTO.builder()
                .accessToken(accessToken)
                .accessExpirationTime(accessExpiration.getTime())
                .refreshToken(refreshToken)
                .refreshExpirationTime(refreshExpiration.getTime())
                .build();

    }

    public String generateToken(String email, boolean isAccessToken) {

        Date now = new Date();
        long expiryDuration = isAccessToken ? jwtExpirationMs : jwtRefreshExpirationMs;


        Claims claims = Jwts.claims().setSubject(email);
        claims.put("type", isAccessToken ? "access" : "refresh");
        log.info("jwtSecret" + jwtSecret + "???");
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiryDuration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getProviderIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean tokenValidation(String token, boolean isAccessToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            String type = claims.get("type", String.class);
            return type != null && type.equals(isAccessToken ? "access" : "refresh");
        } catch (SignatureException ex) {
            log.info("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims string is empty");
        }
        return false;
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


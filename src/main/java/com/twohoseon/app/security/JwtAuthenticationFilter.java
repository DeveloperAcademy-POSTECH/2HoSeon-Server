package com.twohoseon.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.dto.ResultDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.member.MemberService;
import com.twohoseon.app.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : JwtAuthenticationFilter
 * @date : 2023/10/07 4:00 PM
 * @modifyed : $
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getHeaderToken(request, "Access");

        String refreshToken = jwtTokenProvider.getHeaderToken(request, "Refresh");

        if (accessToken != null) {
            // 어세스 토큰값이 유효하다면 setAuthentication를 통해
            // security context에 인증 정보저장
            if (jwtTokenProvider.tokenValidation(accessToken, true)) {
                log.info("ProviderId : ", jwtTokenProvider.getProviderIdFromToken(accessToken));
                setAuthentication(jwtTokenProvider.getProviderIdFromToken(accessToken));
            }
            // 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
            else if (refreshToken != null) {
                // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
                boolean isRefreshToken = jwtTokenProvider.refreshTokenValidate(refreshToken);
                // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
                if (isRefreshToken) {
                    // 리프레시 토큰으로 아이디 정보 가져오기
                    String loginId = jwtTokenProvider.getProviderIdFromToken(refreshToken);
                    // 새로운 어세스 토큰 발급
                    String newAccessToken = jwtTokenProvider.generateToken(loginId, true);
                    // 헤더에 어세스 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                    // Security context에 인증 정보 넣기
                    setAuthentication(jwtTokenProvider.getProviderIdFromToken(newAccessToken));
                }
                // 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
                else {

                    jwtExceptionHandler(response,
                            ResultDTO.builder()
                                    .status(StatusEnum.BAD_REQUEST)
                                    .message("RefreshToken Expired")
                                    .build()
                    );
                    return;
                }
            } else {
                jwtExceptionHandler(response,
                        ResultDTO.builder()
                                .status(StatusEnum.BAD_REQUEST)
                                .message("Invalid JWT signature")
                                .build());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String providerId) {
        UserDetails memberDetails = memberService.loadUserByUsername(providerId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Jwt 예외처리
    public void jwtExceptionHandler(HttpServletResponse response, ResultDTO result) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(result.getStatus().getStatusCode());
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}

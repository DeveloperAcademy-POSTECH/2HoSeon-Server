package com.twohoseon.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.dto.ErrorResponse;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.ErrorCode;
import com.twohoseon.app.enums.UserRole;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.member.RefreshTokenRepository;
import com.twohoseon.app.service.member.MemberService;
import com.twohoseon.app.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getHeaderToken(request, "Access");
        //밴 리스트 확인
        boolean isValidToken = refreshTokenRepository.existsByAccessTokenAndIsBannedTrue(accessToken);
        if (isValidToken) {
            jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.EXPIRED_TOKEN_ERROR));
            return;
        }

        //특정 url 요청시
        RequestMatcher skipPath = new AntPathRequestMatcher("/api/profiles/**");

        if (accessToken != null) {
            // 어세스 토큰값이 유효하다면 setAuthentication를 통해
            // security context에 인증 정보저장
            try {
                jwtTokenProvider.tokenValidation(accessToken, true);
                String providerId = jwtTokenProvider.getProviderIdFromToken(accessToken);

                Optional<Member> memberOptional = memberRepository.findByProviderId(providerId);
                if (memberOptional.isEmpty()) {
                    jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.MEMBER_NOT_FOUND_ERROR));
                    return;
                }
                Member member = memberOptional.get();
                log.info("ProviderId : ", providerId);

                setAuthentication(jwtTokenProvider.getProviderIdFromToken(accessToken));
                if (skipPath.matches(request)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (member.getRole() == UserRole.ROLE_ADMIN) {
                    log.info("ROLE_ADMIN");
                } else if (member.getSchool() == null) {
                    jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.NOT_COMPLETED_SIGNUP_ERROR));
                    return;
                }
            } catch (SignatureException ex) {
                log.info("Invalid JWT signature");
                jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.INVALID_SIGNATURE_ERROR));
                return;
            } catch (MalformedJwtException ex) {
                log.info("Invalid JWT token");
                jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.MALFORMED_TOKEN_ERROR));
                return;
            } catch (ExpiredJwtException ex) {
                log.info("Expired JWT token");
                jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.EXPIRED_TOKEN_ERROR));
                return;
            } catch (UnsupportedJwtException ex) {
                log.info("Unsupported JWT token");
                jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.UNSUPPORTED_TOKEN_ERROR));
                return;
            } catch (IllegalArgumentException ex) {
                log.info("JWT claims string is empty");
                jwtExceptionHandler(response, ErrorResponse.of(ErrorCode.EMPTY_CLAIMS_ERROR));
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
    public void jwtExceptionHandler(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}

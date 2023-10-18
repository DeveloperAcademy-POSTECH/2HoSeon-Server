package com.twohoseon.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.dto.GeneralResponseDTO;
import com.twohoseon.app.entity.Member;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.UserRole;
import com.twohoseon.app.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getHeaderToken(request, "Access");


        if (accessToken != null) {
            // 어세스 토큰값이 유효하다면 setAuthentication를 통해
            // security context에 인증 정보저장
            if (jwtTokenProvider.tokenValidation(accessToken, true)) {
                String providerId = jwtTokenProvider.getProviderIdFromToken(accessToken);

                Member member = memberRepository.findByProviderId(providerId)
                        .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
                log.info("ProviderId : ", providerId);

                setAuthentication(jwtTokenProvider.getProviderIdFromToken(accessToken));

                if (member.getRole() == UserRole.ROLE_ADMIN) {
                    log.info("ROLE_ADMIN");
                } else if (member.getSchool() == null) {
                    jwtExceptionHandler(response,
                            GeneralResponseDTO.builder()
                                    .status(StatusEnum.CONFLICT)
                                    .message("UNREGISTERED_USER")
                                    .build());
                    return;
                }
            } else {
                jwtExceptionHandler(response,
                        GeneralResponseDTO.builder()
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
    public void jwtExceptionHandler(HttpServletResponse response, GeneralResponseDTO result) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(result.getStatus().getStatusCode());
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}

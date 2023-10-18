package com.twohoseon.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.dto.GeneralResponseDTO;
import com.twohoseon.app.dto.TokenDTO;
import com.twohoseon.app.entity.Member;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.oauth2.user.CustomOAuth2User;
import com.twohoseon.app.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : OAuth2AuthenticationSuccessHandler
 * @date : 2023/10/07 3:49 PM
 * @modifyed : $
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        CustomOAuth2User oAuth2User = (CustomOAuth2User) oAuth2AuthenticationToken.getPrincipal();

        log.info("oAuth2UserName = {}", oAuth2User.getName());
        log.info("oAuth2UserAttribute = {}", oAuth2User.getAttributes());
        String providerId = oAuth2User.getName();

        Member member = memberRepository.findByProviderId(providerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 회원이 존재하는지 체크
        boolean memberIsExist = memberRepository.existsByProviderId(providerId);
        GeneralResponseDTO generalResponseDTO;
        TokenDTO token = jwtTokenProvider.createAllToken(providerId);
        // 회원이 존재할경우
        if (member.getSchool() == null) {
            log.debug("member.getSchool() = {}", member.getSchool());
            // 회원이 존재하면 jwt token 발행을 시작한다.
            generalResponseDTO = GeneralResponseDTO.builder()
                    .status(StatusEnum.CONFLICT)
                    .message("UNREGISTERED_USER")
                    .data(token)
                    .build();
        } else {
            generalResponseDTO = GeneralResponseDTO.builder()
                    .status(StatusEnum.OK)
                    .message("SUCCESS")
                    .data(token)
                    .build();
        }

        log.info("jwtToken = {}", token.getAccessToken());

        //token 발급
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        response.getWriter().write(new ObjectMapper().writeValueAsString(generalResponseDTO));
    }

}
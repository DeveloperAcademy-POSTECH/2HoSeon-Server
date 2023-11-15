package com.twohoseon.app.service.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.OAuth2Provider;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.oauth2.user.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomOAuth2UserServiceImpl
 * @date : 2023/10/07 1:44 PM
 * @modifyed : $
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements CustomOAuth2Service {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.debug("userRequest = " + userRequest);
        log.debug("registrationId = " + registrationId);

        Map<String, Object> attributes;
        OAuth2User oAuth2User;
        if (registrationId.contains("apple")) {
            String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
            attributes = decodeJwtPayload(idToken);
            attributes.put("id_token", idToken);
            Set<GrantedAuthority> authorities = new LinkedHashSet<>();
            authorities.add(new OAuth2UserAuthority(attributes));
            oAuth2User = new DefaultOAuth2User(authorities, attributes, "sub");

        } else {
            oAuth2User = super.loadUser(userRequest);
        }

        OAuth2Provider oAuth2Provider = OAuth2Provider.getProvider(userRequest);
        Member member = processUser(memberRepository, oAuth2Provider, oAuth2User.getAttributes());
        return new CustomOAuth2User(member.getId(), member.getRole(), member.getProviderId(), oAuth2User);
    }

    private Map<String, Object> decodeJwtPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);

        } catch (JsonProcessingException e) {
            log.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
        }
        log.debug("jwtClaims = " + jwtClaims);
        return jwtClaims;
    }
}

package com.twohoseon.app.security;

import com.twohoseon.app.util.AppleUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomRequestEntityConverter
 * @date : 2023/10/07 2:10 PM
 * @modifyed : $
 **/
@Slf4j
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
    @Autowired
    private AppleUtility appleUtility;
    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;
    @Value("${oauth.apple.key-path}")
    String keyFilePath;
    @Value("${oauth.apple.key-id}")
    String appleKeyId;
    @Value("${oauth.apple.client-id}")
    String appleClientId;
    @Value("${oauth.apple.team-id}")
    String appleTeamId;


    public CustomRequestEntityConverter() {
        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
        log.debug("req = " + req);
        RequestEntity<?> entity = defaultConverter.convert(req);
        String registrationId = req.getClientRegistration().getRegistrationId();
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
        //Apple일 경우 secret key를 동적으로 세팅
        if (registrationId.contains("apple")) {
            params.set("client_secret", appleUtility.getAppleClientSecret());
        }
        return new RequestEntity<>(params, entity.getHeaders(),
                entity.getMethod(), entity.getUrl());
    }

}
package com.twohoseon.app.service.apple;

import com.twohoseon.app.util.AppleUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleServiceImpl
 * @date : 11/18/23 12:36 AM
 * @modifyed : $
 **/
@Service
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {
    private final RestTemplate restTemplate;
    private final AppleUtility appleUtility;

    @Override
    public void revokeAppleToken(String appleRefreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", appleUtility.getAppleClientId());
        map.add("client_secret", appleUtility.getAppleClientSecret());
        map.add("token", appleRefreshToken);
        map.add("token_type_hint", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://appleid.apple.com/auth/revoke";
        restTemplate.postForLocation(url, request);
    }

    @Override
    public void listenDeleteMemberEvent(String payload) {
        System.out.println(payload);
    }
}

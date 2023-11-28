package com.twohoseon.app.service.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twohoseon.app.dto.apple.AppleEvent;
import com.twohoseon.app.dto.apple.ApplePublicKeyResponse;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.service.member.MemberService;
import com.twohoseon.app.util.AppleUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleServiceImpl
 * @date : 11/18/23 12:36 AM
 * @modifyed : $
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final AppleUtility appleUtility;
    private final MemberService memberService;


    @Override
    public void listenDeleteMemberEvent(String payload) {
        log.debug("payload = " + payload);
        PublicKey applePublicKey = null;
        AppleEvent appleEvent = null;
        try {
            ApplePublicKeyResponse applePublicKeyResponse = getApplePublicKey();
            Map<String, String> tokenHeaders = extractJWTHeaderToMap(payload);
            applePublicKey = appleUtility.getApplePublicKey(tokenHeaders, applePublicKeyResponse);
            log.debug("applePublicKey = " + applePublicKey);

            Jws<Claims> jws = Jwts.parser().setSigningKey(applePublicKey).parseClaimsJws(payload);
            // Get the JWT payload
            Claims claims = jws.getBody();
            log.debug("claims : {}", claims);
            appleEvent = new ObjectMapper().readValue(claims.get("events").toString(), AppleEvent.class);
            log.debug("appleEvent.getSub() = " + appleEvent.getSub());
            log.debug("appleEvent.getType() = " + appleEvent.getType());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        switch (appleEvent.getType()) {
            case ACCOUNT_DELETE -> {
                log.debug("delete apple user");
                memberService.deleteAppleMember(appleEvent.getSub());
            }
            case CONSENT_REVOKED -> {
                log.debug("revoke apple user");
                memberService.deleteAppleMember(appleEvent.getSub());
            }
        }
    }

    private ApplePublicKeyResponse getApplePublicKey() {
        String url = "https://appleid.apple.com/auth/keys";
        return restTemplate.getForObject(url, ApplePublicKeyResponse.class);
    }

    private static Map<String, String> extractJWTHeaderToMap(String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length > 1) {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[0]);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(decodedString, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

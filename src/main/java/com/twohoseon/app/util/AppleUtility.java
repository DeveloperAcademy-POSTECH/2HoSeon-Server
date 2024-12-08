package com.twohoseon.app.util;

import com.twohoseon.app.dto.apple.ApplePublicKey;
import com.twohoseon.app.dto.apple.ApplePublicKeyResponse;
import com.twohoseon.app.service.schedule.JobSchedulingService;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;
import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleUtility
 * @date : 11/15/23 4:23 PM
 * @modifyed : $
 **/
@RequiredArgsConstructor
@Slf4j
@Component
public class AppleUtility {
    private final JobSchedulingService jobSchedulingService;
    private final RestTemplate restTemplate;

    @Value("${oauth.apple.key-path}")
    String keyFilePath;
    @Value("${oauth.apple.key-id}")
    String appleKeyId;
    @Value("${oauth.apple.client-id}")
    String appleClientId;
    @Value("${oauth.apple.team-id}")
    String appleTeamId;

    private String currentDir;


    private String appleClientSecret;

    public PublicKey getApplePublicKey(Map<String, String> tokenHeaders, ApplePublicKeyResponse applePublicKeys) throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {

        ApplePublicKey applePublicKey = applePublicKeys.getMatchedKey((String) tokenHeaders.get("kid"), (String) tokenHeaders.get("alg"));
        return createApplePublicKey(applePublicKey);
    }

    private PublicKey createApplePublicKey(ApplePublicKey applePublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nByte = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eByte = Base64.getUrlDecoder().decode(applePublicKey.e());
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nByte), new BigInteger(1, eByte));
        KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
        return keyFactory.generatePublic(publicKeySpec);
    }

    @PostConstruct
    public void loadAppleClientSecret() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/ubuntu/client_secret.txt"))) {
            appleClientSecret = reader.readLine();
        } catch (IOException e) {
            log.error("Error_loadAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
            appleClientSecret = createAppleClientSecret();
        }
    }

    public void revokeAppleToken(String appleRefreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", appleClientId);
        map.add("client_secret", appleClientSecret);
        map.add("token", appleRefreshToken);
        map.add("token_type_hint", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://appleid.apple.com/auth/revoke";
        restTemplate.postForLocation(url, request);
    }

    public String createAppleClientSecret() {
        String clientSecret = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);

        try {
            InputStream inputStream = new ClassPathResource(keyFilePath).getInputStream();
            File file = File.createTempFile("appleKeyFile", ".p8");
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }


            PEMParser pemParser = new PEMParser(new FileReader(file));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();

            PrivateKey privateKey = converter.getPrivateKey(privateKeyInfo);

            clientSecret = Jwts.builder()
                    .setHeaderParam(JwsHeader.KEY_ID, appleKeyId)
                    .setIssuer(appleTeamId)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(appleClientId)
                    .setExpiration(calendar.getTime())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact();

            // Save the client secret to a file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("/home/ubuntu/client_secret.txt"))) {
                writer.write(clientSecret);
            } catch (IOException e) {
                log.error("Error_saveAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
            }
            jobSchedulingService.refreshAppleSecretJob();
        } catch (IOException e) {
            log.error("Error_createAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        log.debug("createAppleClientSecret : {}", clientSecret);
        return clientSecret;
    }

    public String getAppleClientSecret() {
        if (appleClientSecret == null) {
            appleClientSecret = createAppleClientSecret();
        }
        return appleClientSecret;
    }

}

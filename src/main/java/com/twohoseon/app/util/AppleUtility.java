package com.twohoseon.app.util;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Date;

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
    @Value("${oauth.apple.key-path}")
    String keyFilePath;
    @Value("${oauth.apple.key-id}")
    String appleKeyId;
    @Value("${oauth.apple.client-id}")
    String appleClientId;
    @Value("${oauth.apple.team-id}")
    String appleTeamId;

    public String createAppleClientSecret() {
        String clientSecret = "";
        //application-oauth.yml에 설정해놓은 apple secret Key를 /를 기준으로 split

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
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact();

        } catch (IOException e) {

            log.error("Error_createAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
        }
        log.info("createAppleClientSecret : {}", clientSecret);
        return clientSecret;
    }

    public String getAppleClientId() {
        return appleClientId;
    }
}

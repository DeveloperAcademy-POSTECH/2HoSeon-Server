package com.twohoseon.app.util;

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
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.PrivateKey;
import java.util.Calendar;
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
    private final JobSchedulingService jobSchedulingService;
    @Value("${oauth.apple.key-path}")
    String keyFilePath;
    @Value("${oauth.apple.key-id}")
    String appleKeyId;
    @Value("${oauth.apple.client-id}")
    String appleClientId;
    @Value("${oauth.apple.team-id}")
    String appleTeamId;

    private String appleClientSecret;

    @PostConstruct
    public void loadAppleClientSecret() {
        try (BufferedReader reader = new BufferedReader(new FileReader("client_secret.txt"))) {
            appleClientSecret = reader.readLine();
        } catch (IOException e) {
            log.error("Error_loadAppleClientSecret : {}-{}", e.getMessage(), e.getCause());
            appleClientSecret = createAppleClientSecret();
        }
    }

    public String createAppleClientSecret() {
        String clientSecret = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
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
                    .setExpiration(calendar.getTime())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact();

            // Save the client secret to a file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("client_secret.txt"))) {
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
        log.info("createAppleClientSecret : {}", clientSecret);
        return clientSecret;
    }

    public String getAppleClientSecret() {
        if (appleClientSecret == null) {
            appleClientSecret = createAppleClientSecret();
        }
        return appleClientSecret;
    }

    public String getAppleClientId() {
        return appleClientId;
    }
}

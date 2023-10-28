package com.twohoseon.app.config;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ApnsConfig
 * @date : 10/29/23 2:04 AM
 * @modifyed : $
 **/
@Configuration
@RequiredArgsConstructor
public class ApnsConfig {

    @Value("${app.apple.team-id}")
    private String teamId;

    @Value("${app.apple.key-id}")
    private String keyId;
    @Value("${app.apple.key-path}")
    private String keyPath;
    private final ResourceLoader resourceLoader;


    @Bean
    public ApnsClient apnsClient() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        InputStream inputStream = resourceLoader.getResource("classpath:" + keyPath).getInputStream();
        File keyFile = File.createTempFile("key", ".p8");

        try (OutputStream outputStream = new FileOutputStream(keyFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST, ApnsClientBuilder.DEFAULT_APNS_PORT)
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(keyFile, teamId, keyId))
                .build();
    }
}

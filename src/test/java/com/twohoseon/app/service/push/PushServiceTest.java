package com.twohoseon.app.service.push;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PushServiceTest
 * @date : 10/29/23 12:37 AM
 * @modifyed : $
 **/

@SpringBootTest
class PushServiceTest {
    @Autowired
    private ApnsClient apnsClient;

    @Test
    public void pushTest() throws IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException {

        final String deviceToken = "80df8cbcc1a556d6cef530c6eacfb0b3e33e54e26010328769b2f83ee3a7d445645c94a04de6ba31b78a0c27cd0de712e1f06e07df284635ab9c51972e7130b28c41f3c2240e9c98a379f10e44ed1574"; // Replace with the actual device token

        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                TokenUtil.sanitizeTokenString(deviceToken),
                "com.twohosun.TwoHoSun",
                new SimpleApnsPayloadBuilder()
                        .setAlertBody("헬로월드")
                        .build()
        );

        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                apnsClient.sendNotification(pushNotification).get();

        assertTrue(pushNotificationResponse.isAccepted(), "Push notification should be accepted by APNs gateway");

        apnsClient.close();
    }
}
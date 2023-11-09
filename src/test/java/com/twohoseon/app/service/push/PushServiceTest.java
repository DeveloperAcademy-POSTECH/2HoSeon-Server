package com.twohoseon.app.service.push;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.twohoseon.app.dto.apns.CustomApnsPayloadBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void pushTest() throws ExecutionException, InterruptedException {

        final String deviceToken = "2db472571a37892fa24184642d4aaa15a9b48973346144d68aff5a2985cfc5bf"; // Replace with the actual device token

        final SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                TokenUtil.sanitizeTokenString(deviceToken),
                "com.twohosun.TwoHoSun",
                new CustomApnsPayloadBuilder()
                        .setPostDetails("1")
                        .setAlertBody("뿌지지지지지지지지지지지지지지직")
                        .setSound("default")
                        .build()
        );

        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                apnsClient.sendNotification(pushNotification).get();
        apnsClient.sendNotification(pushNotification).get();


        assertTrue(pushNotificationResponse.isAccepted(), "Push notification should be accepted by APNs gateway");
        apnsClient.close();
    }
}
package com.twohoseon.app.service.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.DeliveryPriority;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.PushType;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.twohoseon.app.dto.apns.CustomApnsPayloadBuilder;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.repository.device.token.DeviceTokenRepository;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NotificationServiceImpl
 * @date : 11/1/23 2:40 AM
 * @modifyed : $
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ApnsClient apnsClient;
    private final MemberRepository memberRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final PostRepository postRepository;

    @Value("${app.identifier}")
    private String appIdentifier;


    @Override
    public void sendPostExpiredNotification(Post post) throws ExecutionException, InterruptedException {
        List<String> deviceTokens = deviceTokenRepository.findAllByMemberId(post.getAuthor().getId());
        for (String deviceToken : deviceTokens) {
            String alertBody = String.format("%s 투표가 종료 되었어요.", post.getTitle());
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                    TokenUtil.sanitizeTokenString(deviceToken),
                    appIdentifier,
                    new CustomApnsPayloadBuilder()
                            .setPostDetails(post)
                            .setAlertBody(alertBody)
                            .setSound("default")
                            .build()
            );
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    apnsClient.sendNotification(pushNotification).get();
            log.debug(pushNotification.toString());
            log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
        }
    }

    @Override
    public void sendPostCommentNotification(Post post, String userNickname) throws ExecutionException, InterruptedException {
        List<String> deviceTokens = deviceTokenRepository.findAllByMemberId(post.getAuthor().getId());
        //Sub comment 핸들링

        String alertBody = String.format("%s님이 회원님의 게시글에 댓글을 달았어요", userNickname);

        for (String deviceToken : deviceTokens) {
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                    TokenUtil.sanitizeTokenString(deviceToken),
                    appIdentifier,
                    new CustomApnsPayloadBuilder()
                            .setPostDetails(post)
                            .setAlertSubtitle(post.getTitle())
                            .setAlertBody(alertBody)
                            .setSound("default")
                            .build()
            );
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    apnsClient.sendNotification(pushNotification).get();
            log.debug(pushNotification.toString());
            log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
        }
    }

    @Override
    public void sendPostSubCommentNotification(Comment parentComment, String userNickname) throws ExecutionException, InterruptedException {
        List<String> deviceTokens = deviceTokenRepository.findAllByMemberId(parentComment.getAuthor().getId());
        //Sub comment 핸들링

        String alertBody = String.format("%s님이 회원님의 댓글에 답글을 달았어요", userNickname);
        for (String deviceToken : deviceTokens) {
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                    TokenUtil.sanitizeTokenString(deviceToken),
                    appIdentifier,
                    new CustomApnsPayloadBuilder()
                            .setPostDetails(parentComment.getPost())
                            .setAlertSubtitle(parentComment.getPost().getTitle())
                            .setAlertBody(alertBody)
                            .setSound("default")
                            .build()
            );
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    apnsClient.sendNotification(pushNotification).get();
            log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
            log.debug(pushNotification.toString());
        }
    }

    @Override
    public void sendPostReviewNotification(Post post) throws ExecutionException, InterruptedException {
        List<String> deviceTokens = deviceTokenRepository.findAllByPostId(post.getId());

        for (String deviceToken : deviceTokens) {
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                    TokenUtil.sanitizeTokenString(deviceToken),
                    appIdentifier,
                    new CustomApnsPayloadBuilder()
                            .setPostDetails(post)
                            .setAlertSubtitle(post.getTitle())
                            .setAlertBody("회원님의 투표에 후기가 작성되었어요.")
                            .setSound("default")
                            .build()
            );
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    apnsClient.sendNotification(pushNotification).get();
            log.debug(pushNotification.toString());
            log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
        }
    }

    @Override
    public void sendConsumerTypeNotification(Member reqMember) throws ExecutionException, InterruptedException {
        List<String> deviceTokens = deviceTokenRepository.findAllByMemberId(reqMember.getId());
        for (String deviceToken : deviceTokens) {
            log.debug("deviceToken: " + deviceToken);
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                    TokenUtil.sanitizeTokenString(deviceToken),
                    appIdentifier,
                    new CustomApnsPayloadBuilder()
                            .setConsumerTypeExist(true)
                            .setContentAvailable(true)
                            .build(),
                    Instant.now().plusSeconds(24 * 60 * 60), // 만료 시간 (예: 24시간 후)
                    DeliveryPriority.IMMEDIATE, // 우선 순위
                    PushType.BACKGROUND // 푸시 유형
            );
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    apnsClient.sendNotification(pushNotification).get();
            log.debug(pushNotification.toString());
            log.debug("push notification is success?: " + pushNotificationResponse.isAccepted());
        }
    }

}

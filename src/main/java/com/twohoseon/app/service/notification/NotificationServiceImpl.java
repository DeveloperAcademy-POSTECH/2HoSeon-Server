package com.twohoseon.app.service.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.twohoseon.app.dto.apns.CustomApnsPayloadBuilder;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.MemberNotFoundException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private final PostRepository postRepository;
//    private final Scheduler scheduler;

    @Value("${app.identifier}")
    private String appIdentifier;


    @Override
    public void sendPostExpiredNotification(Long memberId, Long postId) throws ExecutionException, InterruptedException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        String deviceToken = member.getDeviceToken();
        String alertBody = String.format("%s 투표가 종료 되었어요.", post.getTitle());
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                TokenUtil.sanitizeTokenString(deviceToken),
                appIdentifier,
                new CustomApnsPayloadBuilder()
                        .setPostDetails(postId)
                        .setAlertBody(alertBody)
                        .build()
        );
        PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                apnsClient.sendNotification(pushNotification).get();
        log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
    }

    @Override
    public void sendPostCommentNotification(Post post, String userNickname, boolean isSubComment) throws ExecutionException, InterruptedException {
        Member Author = post.getAuthor();
        String deviceToken = Author.getDeviceToken();
        String alertBody = isSubComment ?
                String.format("%s님이 회원님의 댓글에 답글을 달았어요", userNickname) :
                String.format("%s님이 회원님의 게시글에 댓글을 달았어요", userNickname);
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(
                TokenUtil.sanitizeTokenString(deviceToken),
                appIdentifier,
                new CustomApnsPayloadBuilder()
                        .setPostDetails(post.getId())
                        .setAlertSubtitle(post.getTitle())
                        .setAlertBody(alertBody)
                        .build()
        );
        PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                apnsClient.sendNotification(pushNotification).get();
        log.debug("push notification is success?: ", pushNotificationResponse.isAccepted());
    }

}

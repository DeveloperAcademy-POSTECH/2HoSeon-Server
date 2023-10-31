package com.twohoseon.app.service.notification;

import com.twohoseon.app.entity.member.Member;

import java.util.concurrent.ExecutionException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NotificationService
 * @date : 11/1/23 2:39 AM
 * @modifyed : $
 **/

public interface NotificationService {
    //TODO 투표에 댓글이 달렸을 때
    //TODO 투표가 마감 되었을 때
    //TODO 후기에 댓글이 달렸을 때

    void postExpireNotificationJob(Member member, Long postId);

    void sendPostExpiredNotification(Long memberId, Long postId) throws ExecutionException, InterruptedException;
}

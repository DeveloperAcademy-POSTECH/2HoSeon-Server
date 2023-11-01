package com.twohoseon.app.service.notification;

import com.twohoseon.app.entity.post.Post;

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
    void sendPostExpiredNotification(Long memberId, Long postId) throws ExecutionException, InterruptedException;

    void sendPostCommentNotification(Post post, String userNickname, boolean isSubComment) throws ExecutionException, InterruptedException;

    //TODO 후기에 댓글이 달렸을 때
    //TODO 후기에 답글이 달렸을 때
    //TODO 구독한 후기가 작성되었을 때
}

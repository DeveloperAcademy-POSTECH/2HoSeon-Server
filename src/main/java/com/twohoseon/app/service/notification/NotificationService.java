package com.twohoseon.app.service.notification;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Comment;
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
    void sendPostExpiredNotification(Post post) throws ExecutionException, InterruptedException;

    void sendPostCommentNotification(Post post, String userNickname, String profileImage) throws ExecutionException, InterruptedException;

    void sendPostSubCommentNotification(Comment parentComment, String userNickname, String profileImage) throws ExecutionException, InterruptedException;

    void sendPostReviewNotification(Post post) throws ExecutionException, InterruptedException;

    void sendConsumerTypeNotification(Member reqMember) throws ExecutionException, InterruptedException;
}

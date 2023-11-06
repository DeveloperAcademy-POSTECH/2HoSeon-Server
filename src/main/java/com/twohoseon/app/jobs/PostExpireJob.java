package com.twohoseon.app.jobs;

import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostExpireJob
 * @date : 11/1/23 2:20 AM
 * @modifyed : $
 **/

public class PostExpireJob implements Job {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Long postId = dataMap.getLong("postId");
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setPostToComplete();
        try {
            notificationService.sendPostExpiredNotification(post);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("SampleJob.execute");
    }
}

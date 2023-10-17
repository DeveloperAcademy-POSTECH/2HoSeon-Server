package com.twohoseon.app.repository.handler;

import com.twohoseon.app.entity.Member;
import com.twohoseon.app.entity.post.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostEventHandler
 * @date : 10/17/23 10:21 PM
 * @modifyed : $
 **/
@Slf4j
@Component
@RepositoryEventHandler
public class PostEventHandler {
    @HandleBeforeCreate(Post.class)
    public void handlePostCreate(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        log.debug("member : {}", member);
        log.info("제발 되라");
    }

    @HandleBeforeSave(Post.class)
    public void handlePostSave(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        log.debug("member : {}", member);
        log.info("제발 되라");
    }
}

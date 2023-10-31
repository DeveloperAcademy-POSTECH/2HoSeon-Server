package com.twohoseon.app.service.post;

import jakarta.transaction.Transactional;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostLikeService
 * @date : 2023/10/18
 * @modifyed : $
 **/

public interface PostLikeService {

    @Transactional
    void likePost(Long postId);

    @Transactional
    void unlikePost(Long postId);
}

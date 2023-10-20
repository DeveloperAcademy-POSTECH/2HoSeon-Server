package com.twohoseon.app.service.post;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostLikeService
 * @date : 2023/10/18
 * @modifyed : $
 **/

public interface PostLikeService {

    void insert(Long postId);

    void delete(Long postId);
}

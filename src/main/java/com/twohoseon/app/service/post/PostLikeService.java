package com.twohoseon.app.service.post;

import com.twohoseon.app.service.CommonService;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostLikeService
 * @date : 2023/10/18
 * @modifyed : $
 **/

public interface PostLikeService extends CommonService {

    void likePost(Long postId);

    void unlikePost(Long postId);
}

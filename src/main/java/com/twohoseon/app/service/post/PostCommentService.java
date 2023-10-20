package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.PostCommentRequestDTO;
import com.twohoseon.app.entity.post.PostComment;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentService
 * @date : 2023/10/18
 * @modifyed : $
 **/
public interface PostCommentService {

    void commentCreate(PostCommentRequestDTO postCommentRequestDTO);

    List<PostComment> commentRead(Long postId);
}

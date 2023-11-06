package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.comment.CommentCreateRequestDTO;
import com.twohoseon.app.dto.response.PostCommentInfoDTO;
import com.twohoseon.app.service.CommonService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentService
 * @date : 2023/10/18
 * @modifyed : $
 **/
public interface PostCommentService extends CommonService {


    @Transactional
    void createComment(CommentCreateRequestDTO commentCreateRequestDTO);

    @Transactional
    void deleteComment(Long postCommentId);

    @Transactional
    void updateComment(Long postCommentId, String content);

    List<PostCommentInfoDTO> getPostCommentChildren(Long postId, Long commentId);
}

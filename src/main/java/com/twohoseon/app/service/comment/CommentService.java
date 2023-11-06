package com.twohoseon.app.service.comment;

import com.twohoseon.app.dto.request.comment.CommentCreateRequestDTO;
import com.twohoseon.app.dto.request.comment.SubCommentCreateRequestDTO;
import com.twohoseon.app.dto.response.CommentInfoDTO;
import com.twohoseon.app.service.CommonService;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentService
 * @date : 2023/10/18
 * @modifyed : $
 **/
public interface CommentService extends CommonService {


    void createComment(CommentCreateRequestDTO commentCreateRequestDTO);

    void createSubComment(Long commentId, SubCommentCreateRequestDTO subCommentCreateRequestDTO);

    void deleteComment(Long postCommentId);

    void updateComment(Long postCommentId, String content);

    List<CommentInfoDTO> getPostComments(Long postId);

    List<CommentInfoDTO> getSubComments(Long commentId);
}

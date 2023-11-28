package com.twohoseon.app.service.comment;

import com.twohoseon.app.dto.request.comment.CommentCreateRequest;
import com.twohoseon.app.dto.request.comment.SubCommentCreateRequest;
import com.twohoseon.app.dto.response.CommentInfo;
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


    void createComment(CommentCreateRequest commentCreateRequest);

    void createSubComment(Long commentId, SubCommentCreateRequest subCommentCreateRequest);

    void deleteComment(Long postCommentId);

    void updateComment(Long postCommentId, String content);

    List<CommentInfo> getPostComments(Long postId);

    List<CommentInfo> getSubComments(Long commentId);
}

package com.twohoseon.app.repository.comment;

import com.twohoseon.app.dto.response.CommentInfo;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentCustomRepository
 * @date : 11/6/23 10:49 PM
 * @modifyed : $
 **/
public interface CommentCustomRepository {
    List<CommentInfo> getAllCommentsFromPost(Long postId);

    List<CommentInfo> getSubComments(Long parentId);
}

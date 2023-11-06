package com.twohoseon.app.repository.comment;

import com.twohoseon.app.dto.response.CommentInfoDTO;

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
    List<CommentInfoDTO> getAllCommentsFromPost(Long postId);

    List<CommentInfoDTO> getSubComments(Long parentId);
}

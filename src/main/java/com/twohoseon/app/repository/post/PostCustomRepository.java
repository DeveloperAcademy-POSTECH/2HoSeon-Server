package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.PostCommentInfoDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCustomRepository
 * @date : 10/19/23 10:37 PM
 * @modifyed : $
 **/
public interface PostCustomRepository {
    public List<PostInfoDTO> findAllPostsInMainPage(Pageable pageable);

    List<PostCommentInfoDTO> getAllCommentsFromPost(Long postId);

    
}

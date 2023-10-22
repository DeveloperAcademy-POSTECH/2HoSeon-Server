package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.PostCommentInfoDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.entity.post.enums.PostStatus;
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
    List<PostInfoDTO> findAllPosts(Pageable pageable, PostStatus postStatus, long memberId);

//    PostInfoDTO findPostById(long postId);

    PostInfoDTO findPostById(long postId, long memberId);

    List<PostCommentInfoDTO> getAllCommentsFromPost(Long postId);

    VoteCountsDTO getVoteInfo(long postId);
}

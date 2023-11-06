package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.CommentInfoDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.enums.post.PostStatus;
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

    List<CommentInfoDTO> getAllCommentsFromPost(Long postId);

    List<PostInfoDTO> findAllPostsByKeyword(Pageable pageable, String keyword, long memberId);

    List<CommentInfoDTO> getChildComments(Long parentId);

    VoteCountsDTO getVoteInfo(long postId);


}

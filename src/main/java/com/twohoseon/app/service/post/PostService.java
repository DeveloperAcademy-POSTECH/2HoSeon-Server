package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.service.CommonService;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostService
 * @date : 10/18/23 11:17 PM
 * @modifyed : $
 **/

public interface PostService extends CommonService {

    void createPost(PostRequestDTO postRequestDTO);

    public List<PostInfoDTO> fetchPosts(Pageable pageable, PostStatus postStatus);


    VoteCountsDTO createVote(Long postId, VoteType voteType);

//    VoteCountsDTO getVoteCounts(Long postId);

    PostInfoDTO fetchPost(Long postId);

    void updatePost(Long postId, PostRequestDTO postRequestDTO);

    void deletePost(Long postId);

    void createReview(Long postId, ReviewRequestDTO reviewRequestDTO);

    void updateReview(Long postId, ReviewRequestDTO reviewRequestDTO);

    void deleteReview(Long postId);

    void subscribePost(Long postId);
}

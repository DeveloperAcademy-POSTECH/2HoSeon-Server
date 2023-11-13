package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.dto.response.mypage.MypageFetch;
import com.twohoseon.app.dto.response.post.ReviewDetail;
import com.twohoseon.app.dto.response.post.ReviewFetch;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.service.CommonService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

    void createPost(PostRequestDTO postRequestDTO, MultipartFile file);

    public List<PostInfoDTO> fetchPosts(Pageable pageable, VisibilityScope visibilityScope);


    VoteCountsDTO createVote(Long postId, VoteType voteType);

//    VoteCountsDTO getVoteCounts(Long postId);

    PostInfoDTO fetchPost(Long postId);

    void updatePost(Long postId, PostRequestDTO postRequestDTO, MultipartFile file);

    void deletePost(Long postId);

    void createReview(Long postId, ReviewRequestDTO reviewRequestDTO, MultipartFile file);

    void updateReview(Long postId, ReviewRequestDTO reviewRequestDTO, MultipartFile file);

    void deleteReview(Long postId);

    void subscribePost(Long postId);

    ReviewFetch fetchReviews(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType);

    MypageFetch fetchMyReviews(VisibilityScope visibilityScope, Pageable pageable);

    ReviewDetail getReviewDetail(Long postId);

    MypageFetch fetchMypagePosts(Pageable pageable, MyVoteCategoryType myVoteCategoryType);
}

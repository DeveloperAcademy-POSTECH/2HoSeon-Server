package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequest;
import com.twohoseon.app.dto.request.review.ReviewRequest;
import com.twohoseon.app.dto.response.VoteCounts;
import com.twohoseon.app.dto.response.mypage.MypageFetch;
import com.twohoseon.app.dto.response.post.*;
import com.twohoseon.app.enums.ReviewType;
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

    void createPost(PostRequest postRequest, MultipartFile file);

    void completeVote(Long postId);

    public List<PostInfo> fetchPosts(Pageable pageable, VisibilityScope visibilityScope);


    VoteCounts createVote(Long postId, boolean myChoice);

//    VoteCountsDTO getVoteCounts(Long postId);

    PostDetail fetchPostDetail(Long postId);

    void updatePost(Long postId, PostRequest postRequest, MultipartFile file);

    void deletePost(Long postId);

    void createReview(Long postId, ReviewRequest reviewRequest, MultipartFile file);

    void updateReview(Long postId, ReviewRequest reviewRequest, MultipartFile file);

    void deleteReview(Long postId);

    void subscribePost(Long postId);

    void unsubscribePost(Long postId);

//    AllReviewFetch fetchReviewsTemp(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType);

    MypageFetch fetchMyReviews(VisibilityScope visibilityScope, Pageable pageable);

    ReviewDetail fetchReviewDetail(Long postId);

    ReviewDetail fetchReviewDetailByReviewId(Long reviewId);

    MypageFetch fetchMypagePosts(Pageable pageable, MyVoteCategoryType myVoteCategoryType);

    AllReviewFetch fetchAllReviews(VisibilityScope visibilityScope);

    List<PostSummary> fetchReviews(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType);
}

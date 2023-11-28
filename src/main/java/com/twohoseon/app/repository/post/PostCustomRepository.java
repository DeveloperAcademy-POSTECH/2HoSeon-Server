package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.VoteCounts;
import com.twohoseon.app.dto.response.post.PostDetail;
import com.twohoseon.app.dto.response.post.PostInfo;
import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.VisibilityScope;
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
    List<PostInfo> findAllPosts(Pageable pageable, Member memberId, VisibilityScope visibilityScope);

//    PostInfoDTO findPostById(long postId);

    PostInfo findPostById(Long postId, long memberId);

    VoteCounts getVoteInfo(long postId);

    List<PostSummary> findActivePostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword);

    List<PostSummary> findClosedPostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword);

    List<PostSummary> findReviewPostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword);


    List<PostSummary> findRecentReviews(VisibilityScope visibilityScope, Member reqMember, ConsumerType consumerType);

    List<PostSummary> findReviews(Pageable pageable, Member reqMember, VisibilityScope visibilityScope, ReviewType reviewType);

    List<PostSummary> findReviewsById(Pageable pageable, Member reqMember, VisibilityScope visibilityScope);

    PostSummary getPostSummaryInReviewDetail(Long postId);

    PostInfo getReviewDetailByPostId(Long postId);

    Long countAllPostsByMyVoteCategoryType(Member reqMember, MyVoteCategoryType myVoteCategoryType);

    List<PostSummary> findAllPostsByMyVoteCategoryType(Pageable pageable, Member reqMember, MyVoteCategoryType myVoteCategoryType);

    long getTotalReviewCount(Member reqMember, VisibilityScope visibilityScope);

    void deleteSubscriptionsFromMember(Member reqMember);

    PostDetail findPostDetailById(Long postId, Long id);

    Integer calculateCommentCountByPostId(Long postId);

    String getCommentPreviewByPostId(Long postId);

    String getCommentPreviewImageByPostId(Long postId);
}

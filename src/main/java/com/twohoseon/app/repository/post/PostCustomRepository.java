package com.twohoseon.app.repository.post;

import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
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
    List<PostInfoDTO> findAllPosts(Pageable pageable, Member memberId, VisibilityScope visibilityScope);

//    PostInfoDTO findPostById(long postId);

    PostInfoDTO findPostById(Long postId, long memberId);

    VoteCountsDTO getVoteInfo(long postId);

    List<PostSummary> findActivePostsByKeyword(VisibilityScope visibilityScope, Pageable pageable, String keyword);

    List<PostSummary> findClosedPostsByKeyword(VisibilityScope visibilityScope, Pageable pageable, String keyword);

    List<PostSummary> findReviewPostsByKeyword(VisibilityScope visibilityScope, Pageable pageable, String keyword);


    List<PostSummary> findRecentReviews(VisibilityScope visibilityScope, ReviewType reviewType, ConsumerType consumerType);

    List<PostSummary> findReviews(Pageable pageable, VisibilityScope visibilityScope, ReviewType reviewType);
}

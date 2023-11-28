package com.twohoseon.app.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.VoteCounts;
import com.twohoseon.app.dto.response.VoteInfo;
import com.twohoseon.app.dto.response.post.AuthorInfo;
import com.twohoseon.app.dto.response.post.PostDetail;
import com.twohoseon.app.dto.response.post.PostInfo;
import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QComment.comment;
import static com.twohoseon.app.entity.post.QPost.post;
import static com.twohoseon.app.entity.post.vote.QVote.vote;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCustomRepositoryImpl
 * @date : 10/19/23 10:40 PM
 * @modifyed : $
 **/
@Slf4j
@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostInfo> findAllPosts(Pageable pageable, Member reqMember, VisibilityScope visibilityScope) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.ne(PostStatus.REVIEW))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));

        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }

        JPAQuery<PostInfo> jpaQuery = jpaQueryFactory
                .select(Projections.constructor(PostInfo.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.visibilityScope,
                        post.postStatus,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.commentCount,
                        post.agreeCount.add(post.disagreeCount),
                        post.price
                ))
                .from(post)
                .leftJoin(post.author, member)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createDate.desc())
                .distinct();

        List<PostInfo> postInfoList = jpaQuery.fetch();
        for (PostInfo postInfo : postInfoList) {
            postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
            postInfo.setMyChoice(getIsVotedPost(postInfo.getPostId(), reqMember.getId()));
        }

        return postInfoList;
    }

    @Override
    public PostInfo findPostById(Long postId, long memberId) {
        PostInfo postInfo = jpaQueryFactory
                .select(Projections.constructor(PostInfo.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.visibilityScope,
                        post.postStatus,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.commentCount,
                        post.agreeCount.add(post.disagreeCount),
                        post.price,
                        post.review.isNotNull()
                ))
                .from(post)
                .where(post.id.eq(postId))
                .leftJoin(post.author, member)
                .orderBy(post.createDate.desc())
                .fetchOne();

        assert postInfo != null;
        Boolean isVoted = getIsVotedPost(postInfo.getPostId(), memberId);
        postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
        postInfo.setMyChoice(isVoted);
        if (postInfo.getAuthor().getId() == memberId)
            postInfo.setVoteInfoList(getVoteInfoList(postId));
        postInfo.setIsNotified(getIsNotified(postId, memberId));
        postInfo.setIsMine(postInfo.getAuthor().getId() == memberId);
        postInfo.setVoteInfoList(getVoteInfoList(postId));
        return postInfo;
    }

    @Override
    public VoteCounts getVoteInfo(long postId) {
        VoteCounts result = jpaQueryFactory
                .select(
                        Projections.constructor(VoteCounts.class,
                                new CaseBuilder()
                                        .when(vote.isAgree.eq(true))
                                        .then(1)
                                        .otherwise(0)
                                        .sum().as("agree_count"),
                                new CaseBuilder()
                                        .when(vote.isAgree.eq(false))
                                        .then(1)
                                        .otherwise(0)
                                        .sum().as("disagree_count")
                        )
                )
                .from(vote)
                .groupBy(vote.post.id)
                .where(vote.post.id.eq(postId))
                .fetchOne();
        return result == null ? new VoteCounts(0, 0) : result;
    }


    @Override
    public List<PostSummary> findActivePostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword) {
        BooleanBuilder whereClause = new BooleanBuilder(post.title.contains(keyword).or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.ACTIVE))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.agreeCount.add(post.disagreeCount),
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.commentCount
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)

                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public List<PostSummary> findClosedPostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword) {
        BooleanBuilder whereClause = new BooleanBuilder(post.title.contains(keyword).or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.CLOSED))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));

        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.agreeCount.add(post.disagreeCount),
                        post.commentCount,
                        post.voteResult,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public List<PostSummary> findReviewPostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword) {
        BooleanBuilder whereClause = new BooleanBuilder(post.title.contains(keyword).or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.viewCount,
                        post.commentCount,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.isPurchased
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public List<PostSummary> findRecentReviews(VisibilityScope visibilityScope, Member reqMember, ConsumerType consumerType) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.author.consumerType.eq(consumerType))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));

        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }

        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        post.postStatus,
                        post.title,
                        post.contents.substring(0, 25),
                        post.isPurchased
                ))
                .from(post)
                .leftJoin(post.author, member)
                .limit(3)
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public List<PostSummary> findReviews(Pageable pageable, Member reqMember, VisibilityScope visibilityScope, ReviewType reviewType) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.visibilityScope.eq(visibilityScope))
                .and(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.author.notIn(reqMember.getBlockedMember()))
                .and(post.author.isBaned.eq(false));


        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        // ReviewType에 따른 조건 추가
        switch (reviewType) {
            case PURCHASED -> whereClause.and(post.isPurchased.isTrue());
            case NOT_PURCHASED -> whereClause.and(post.isPurchased.isFalse());
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.commentCount,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.isPurchased
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public List<PostSummary> findReviewsById(Pageable pageable, Member reqMember, VisibilityScope visibilityScope) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.author.eq(reqMember));

        if (!visibilityScope.equals(VisibilityScope.ALL)) {
            whereClause.and(post.visibilityScope.eq(visibilityScope));
        }

        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        post.postStatus,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.isPurchased
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;
    }

    @Override
    public PostSummary getPostSummaryInReviewDetail(Long postId) {
        PostSummary result = jpaQueryFactory.select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.voteResult,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price
                ))
                .from(post)
                .leftJoin(post.author, member)
                .where(post.id.eq(postId))
                .orderBy(post.createDate.desc())
                .fetchOne();
        return result;
    }

    @Override
    public PostInfo getReviewDetailByPostId(Long postId) {
        PostInfo result = jpaQueryFactory.select(Projections.constructor(PostInfo.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.postStatus,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.isPurchased
                ))
                .from(post)
                .leftJoin(post.author, member)
                .where(post.id.eq(postId))
                .fetchOne();
        return result;
    }

    @Override
    public Long countAllPostsByMyVoteCategoryType(Member reqMember, MyVoteCategoryType myVoteCategoryType) {
        BooleanBuilder whereClause = new BooleanBuilder(post.author.eq(reqMember))
                .and(post.postStatus.ne(PostStatus.REVIEW));

        JPAQuery<Long> jpaQuery = jpaQueryFactory.select(post.count())
                .from(post);
        switch (myVoteCategoryType) {
            case ACTIVE_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.ACTIVE));
            case CLOSED_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.CLOSED));
            case GLOBAL_VOTES -> whereClause.and(post.visibilityScope.eq(VisibilityScope.GLOBAL));
            case SCHOOL_VOTES -> whereClause.and(post.author.school.eq(reqMember.getSchool()))
                    .and(post.visibilityScope.eq(VisibilityScope.SCHOOL));
        }
        jpaQuery.where(whereClause);

        return jpaQuery.fetchFirst();
    }

    @Override
    public List<PostSummary> findAllPostsByMyVoteCategoryType(Pageable pageable, Member reqMember, MyVoteCategoryType myVoteCategoryType) {
        BooleanBuilder whereClause = new BooleanBuilder(post.author.eq(reqMember))
                .and(post.postStatus.ne(PostStatus.REVIEW));

        switch (myVoteCategoryType) {
            case ACTIVE_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.ACTIVE));
            case CLOSED_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.CLOSED));
            case GLOBAL_VOTES -> whereClause.and(post.visibilityScope.eq(VisibilityScope.GLOBAL));
            case SCHOOL_VOTES -> whereClause.and(post.author.school.eq(reqMember.getSchool()))
                    .and(post.visibilityScope.eq(VisibilityScope.SCHOOL));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        post.postStatus,
                        post.voteResult,
                        post.title,
                        post.image,
                        post.contents.substring(0, 25),
                        post.price,
                        post.review.isNotNull()
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(whereClause)
                .orderBy(post.createDate.desc())
                .fetch();
        return result;

    }


    private Boolean getIsVotedPost(long postId, long memberId) {
        return jpaQueryFactory
                .select(vote.isAgree)
                .from(vote)
                .where(vote.post.id.eq(postId)
                        .and(vote.voter.id.eq(memberId)))
                .fetchOne();
    }

    private boolean getIsNotified(long postId, long memberId) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(post.id.eq(postId).and(post.subscribers.any().id.eq(memberId)))
                .fetchOne() != null;
    }

    private List<VoteInfo> getVoteInfoList(long postId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(VoteInfo.class,
                                vote.isAgree,
                                vote.consumerType
                        ))
                .from(vote)
                .where(vote.post.id.eq(postId))
                .fetch();
    }

    @Override
    public long getTotalReviewCount(Member reqMember, VisibilityScope visibilityScope) {

        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.author.eq(reqMember));

        if (!visibilityScope.equals(VisibilityScope.ALL)) {
            whereClause.and(post.visibilityScope.eq(visibilityScope));
        }

        return jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .where(whereClause)
                .fetchOne();
    }

    @Override
    public void deleteSubscriptionsFromMember(Member reqMember) {
        List<Post> posts = jpaQueryFactory.selectFrom(post)
                .where(post.subscribers.any().id.eq(reqMember.getId()))
                .fetch();
        for (Post post : posts) {
            post.getSubscribers().remove(reqMember);
        }
//        postRepository.saveAll(posts);
    }

    @Override
    public PostDetail findPostDetailById(Long postId, Long memberId) {
        PostDetail postDetail;
        PostInfo postInfo;
        Integer commentCount;
        String commentPreview;
        String commentPreviewImage;
        postInfo = jpaQueryFactory
                .select(Projections.constructor(PostInfo.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.visibilityScope,
                        post.postStatus,
                        Projections.constructor(AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.commentCount,
                        post.agreeCount.add(post.disagreeCount),
                        post.price,
                        post.review.isNotNull()
                ))
                .from(post)
                .where(post.id.eq(postId))
                .leftJoin(post.author, member)
                .orderBy(post.createDate.desc())
                .fetchOne();

        if (postInfo == null) {
            throw new PostNotFoundException();
        } else {
            Boolean isVoted = getIsVotedPost(postInfo.getPostId(), memberId);
            postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
            postInfo.setMyChoice(isVoted);
            if (postInfo.getAuthor().getId() == memberId)
                postInfo.setVoteInfoList(getVoteInfoList(postId));
            postInfo.setIsNotified(getIsNotified(postId, memberId));
            postInfo.setIsMine(postInfo.getAuthor().getId() == memberId);
            postInfo.setVoteInfoList(getVoteInfoList(postId));

            commentCount = calculateCommentCountByPostId(postId);
            if (commentCount != null) {
                commentPreview = getCommentPreviewByPostId(postId);
                commentPreviewImage = getCommentPreviewImageByPostId(postId);
                postDetail = new PostDetail(postInfo, commentCount, commentPreview, commentPreviewImage);
                return postDetail;
            }
        }
        return null;
    }

    @Override
    public Integer calculateCommentCountByPostId(Long postId) {
        return jpaQueryFactory
                .select(post.commentCount)
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public String getCommentPreviewByPostId(Long postId) {
        return jpaQueryFactory
                .select(comment.content)
                .from(comment)
                .where(post.id.eq(postId))
                .orderBy(comment.createDate.desc())
                .fetchFirst();
    }

    @Override
    public String getCommentPreviewImageByPostId(Long postId) {
        return jpaQueryFactory
                .select(comment.author.profileImage)
                .from(comment)
                .where(post.id.eq(postId))
                .orderBy(comment.createDate.desc())
                .fetchFirst();
    }


}
package com.twohoseon.app.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.dto.response.VoteInfoDTO;
import com.twohoseon.app.dto.response.post.AuthorInfoDTO;
import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
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
    public List<PostInfoDTO> findAllPosts(Pageable pageable, Member reqMember, VisibilityScope visibilityScope) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.ne(PostStatus.REVIEW)
                .and(post.visibilityScope.eq(visibilityScope))
        );
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }

        JPAQuery<PostInfoDTO> jpaQuery = jpaQueryFactory
                .select(Projections.constructor(PostInfoDTO.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.visibilityScope,
                        post.postStatus,
                        Projections.constructor(AuthorInfoDTO.class,
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

        List<PostInfoDTO> postInfoList = jpaQuery.fetch();
        for (PostInfoDTO postInfoDTO : postInfoList) {
            postInfoDTO.setVoteCounts(getVoteInfo(postInfoDTO.getPostId()));
            postInfoDTO.setIsVoted(getIsVotedPost(postInfoDTO.getPostId(), reqMember.getId()));
        }

        return postInfoList;
    }

    @Override
    public PostInfoDTO findPostById(Long postId, long memberId) {
        PostInfoDTO postInfo = jpaQueryFactory
                .select(Projections.constructor(PostInfoDTO.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.visibilityScope,
                        post.postStatus,
                        Projections.constructor(AuthorInfoDTO.class,
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
                .where(post.id.eq(postId))
                .leftJoin(post.author, member)
                .orderBy(post.createDate.desc())
                .fetchOne();

        assert postInfo != null;
        Boolean isVoted = getIsVotedPost(postInfo.getPostId(), memberId);
        postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
        postInfo.setIsVoted(isVoted);
        if (postInfo.getAuthor().getId() == memberId)
            postInfo.setVoteInfoList(getVoteInfoList(postId));
        postInfo.setIsNotified(getIsNotified(postId, memberId));
        postInfo.setIsMine(postInfo.getAuthor().getId() == memberId);
        postInfo.setVoteInfoList(getVoteInfoList(postId));
        return postInfo;
    }

    @Override
    public VoteCountsDTO getVoteInfo(long postId) {
        VoteCountsDTO result = jpaQueryFactory
                .select(
                        Projections.constructor(VoteCountsDTO.class,
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
                .groupBy(vote.id.post.id)
                .where(vote.id.post.id.eq(postId))
                .fetchOne();
        return result == null ? new VoteCountsDTO(0, 0) : result;
    }


    @Override
    public List<PostSummary> findActivePostsByKeyword(VisibilityScope visibilityScope, Member reqMember, Pageable pageable, String keyword) {
        BooleanBuilder whereClause = new BooleanBuilder((post.title.contains(keyword).or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.ACTIVE))
                .and(post.visibilityScope.eq(visibilityScope))
        );
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfoDTO.class,
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
        BooleanBuilder whereClause = new BooleanBuilder((post.title.contains(keyword).or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.CLOSED))
                .and(post.visibilityScope.eq(visibilityScope))
        );
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfoDTO.class,
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
        BooleanBuilder whereClause = new BooleanBuilder((post.title.contains(keyword)
                .or(post.contents.contains(keyword)))
                .and(post.postStatus.eq(PostStatus.REVIEW))
                .and(post.visibilityScope.eq(visibilityScope))
        );
        if (visibilityScope == VisibilityScope.SCHOOL) {
            whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        List<PostSummary> result = jpaQueryFactory
                .select(Projections.constructor(PostSummary.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfoDTO.class,
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
    public List<PostSummary> findRecentReviews(VisibilityScope visibilityScope, Member reqMember, ReviewType reviewType, ConsumerType consumerType) {
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW)
                .and(post.visibilityScope.eq(visibilityScope)))
                .and(post.postStatus.eq(PostStatus.REVIEW))
                .and(member.consumerType.eq(consumerType));

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
                        post.postStatus,
                        post.title,
                        post.contents.substring(0, 25)
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
        BooleanBuilder whereClause = new BooleanBuilder(post.postStatus.eq(PostStatus.REVIEW)
                .and(post.visibilityScope.eq(visibilityScope)))
                .and(post.postStatus.eq(PostStatus.REVIEW));

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
                        Projections.constructor(AuthorInfoDTO.class,
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
                        Projections.constructor(AuthorInfoDTO.class,
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
    public PostInfoDTO getReviewDetailByPostId(Long postId) {
        PostInfoDTO result = jpaQueryFactory.select(Projections.constructor(PostInfoDTO.class,
                        post.createDate,
                        post.modifiedDate,
                        post.id,
                        Projections.constructor(AuthorInfoDTO.class,
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
        BooleanBuilder whereClause = new BooleanBuilder(post.author.eq(reqMember));

        JPAQuery<Long> jpaQuery = jpaQueryFactory.select(post.count())
                .from(post);
        switch (myVoteCategoryType) {
            case ACTIVE_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.ACTIVE));
            case FINISHED_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.CLOSED));
            case GLOBAL_VOTES -> whereClause.and(post.visibilityScope.eq(VisibilityScope.GLOBAL));
            case SCHOOL_VOTES -> whereClause.and(post.author.school.eq(reqMember.getSchool()));
        }
        jpaQuery.where(whereClause);

        return jpaQuery.fetchFirst();
    }

    @Override
    public List<PostSummary> findAllPostsByMyVoteCategoryType(Pageable pageable, Member reqMember, MyVoteCategoryType myVoteCategoryType) {
        BooleanBuilder whereClause = new BooleanBuilder();
        switch (myVoteCategoryType) {
            case ACTIVE_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.ACTIVE));
            case FINISHED_VOTES -> whereClause.and(post.postStatus.eq(PostStatus.CLOSED));
            case GLOBAL_VOTES -> whereClause.and(post.visibilityScope.eq(VisibilityScope.GLOBAL));
            case SCHOOL_VOTES -> whereClause.and(post.author.school.eq(reqMember.getSchool()));
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
                .where(vote.id.post.id.eq(postId)
                        .and(vote.id.voter.id.eq(memberId)))
                .fetchOne();
    }

    private boolean getIsNotified(long postId, long memberId) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(post.id.eq(postId).and(post.subscribers.any().id.eq(memberId)))
                .fetchOne() != null;
    }

    private List<VoteInfoDTO> getVoteInfoList(long postId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(VoteInfoDTO.class,
                                vote.isAgree,
                                vote.consumerType
                        ))
                .from(vote)
                .where(vote.id.post.id.eq(postId))
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


}
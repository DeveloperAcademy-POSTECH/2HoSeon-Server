package com.twohoseon.app.repository.post;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.*;
import com.twohoseon.app.entity.post.enums.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QPost.post;
import static com.twohoseon.app.entity.post.QPostComment.postComment;
import static com.twohoseon.app.entity.post.vote.QVote.vote;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCustomRepositoryImpl
 * @date : 10/19/23 10:40 PM
 * @modifyed : $
 **/
@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostInfoDTO> findAllPosts(Pageable pageable, PostStatus postStatus, long memberId) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        JPAQuery<PostInfoDTO> jpaQuery = jpaQueryFactory
                .select(Projections.constructor(PostInfoDTO.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.postType,
                        Projections.constructor(AuthorInfoDTO.class,
                                member.id,
                                member.userNickname,
                                member.userProfileImage),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.likeCount,
                        post.viewCount,
                        post.commentCount,
                        post.postCategoryType
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createDate.desc())
                .distinct();
        if (postStatus == PostStatus.ACTIVE) {
            jpaQuery.where(post.createDate.after(oneDayAgo));
        } else {
            jpaQuery.where(post.createDate.before(oneDayAgo));
        }
        List<PostInfoDTO> postInfoList = jpaQuery.fetch();
        for (PostInfoDTO postInfoDTO : postInfoList) {
            postInfoDTO.setVoteCounts(getVoteInfo(postInfoDTO.getPostId()));
            postInfoDTO.setIsVoted(getIsVotedPost(postInfoDTO.getPostId(), memberId));
            postInfoDTO.setIsMine(postInfoDTO.getAuthor().getId() == memberId);
            postInfoDTO.setPostStatus(postStatus);
            //TODO 여기서 계산후 값을 넣어주면 된다.
//            Date oneDayAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
//
//            return queryFactory
//                    .selectFrom(post)
//                    .where(post.creationDate.after(oneDayAgo))
//                    .fetch();
        }

        return postInfoList;
    }

    @Override
    public PostInfoDTO findPostById(long postId, long memberId) {
        //추가할 곳
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        PostInfoDTO postInfo = jpaQueryFactory
                .select(Projections.constructor(PostInfoDTO.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.postType,
                        post.createDate.after(oneDayAgo),
                        Projections.constructor(AuthorInfoDTO.class,
                                member.id,
                                member.userNickname,
                                member.userProfileImage),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.likeCount,
                        post.viewCount,
                        post.commentCount,
                        post.postCategoryType
                ))
                .from(post)
                .where(post.id.eq(postId))
                .leftJoin(post.author, member)
                .orderBy(post.createDate.desc())
                .fetchOne();

        boolean isVoted = getIsVotedPost(postInfo.getPostId(), memberId);
        postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
        postInfo.setIsVoted(isVoted);
        if (postInfo.getAuthor().getId() == memberId)
            postInfo.setVoteInfoList(getVoteInfoList(postId));
        postInfo.setIsMine(postInfo.getAuthor().getId() == memberId);
        return postInfo;
    }

    @Override
    public List<PostCommentInfoDTO> getAllCommentsFromPost(Long postId) {
        List<PostCommentInfoDTO> postCommentInfoDTOS = jpaQueryFactory.select(Projections.constructor(
                        PostCommentInfoDTO.class,
                        postComment.id,
                        postComment.createDate,
                        postComment.modifiedDate,
                        postComment.content,
                        Projections.constructor(
                                AuthorInfoDTO.class,
                                member.id,
                                member.userNickname,
                                member.userProfileImage
                        )
                ))
                .from(postComment)
                .leftJoin(postComment.author, member)
                .where(
                        postComment.parentComment.isNull()
                                .and(postComment.post.id.eq(postId))
                )
                .orderBy(postComment.createDate.asc())
                .distinct()
                .fetch();

        for (PostCommentInfoDTO comments : postCommentInfoDTOS) {
            comments.setChildComments(getChildComments(comments.getCommentId()));
        }

        return postCommentInfoDTOS;
    }

    @Override
    public List<PostInfoDTO> findAllPostsByKeyword(Pageable pageable, String keyword, long memberId) {

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        JPAQuery<PostInfoDTO> jpaQuery = jpaQueryFactory
                .select(Projections.constructor(
                        PostInfoDTO.class,
                        post.id,
                        post.createDate,
                        post.modifiedDate,
                        post.postType,
                        post.createDate.after(oneDayAgo),
                        Projections.constructor(AuthorInfoDTO.class,
                                member.id,
                                member.userNickname,
                                member.userProfileImage),
                        post.title,
                        post.contents,
                        post.image,
                        post.externalURL,
                        post.likeCount,
                        post.viewCount,
                        post.commentCount,
                        post.postCategoryType
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(post.title.contains(keyword)
                        .or(post.contents.contains(keyword))
                        .or(post.postTagList.contains(keyword)));

        List<PostInfoDTO> postInfoList = jpaQuery.fetch();
        for (PostInfoDTO postInfoDTO : postInfoList) {
            postInfoDTO.setVoteCounts(getVoteInfo(postInfoDTO.getPostId()));
            postInfoDTO.setIsVoted(getIsVotedPost(postInfoDTO.getPostId(), memberId));
            postInfoDTO.setIsMine(postInfoDTO.getAuthor().getId() == memberId);
        }

        return postInfoList;
    }

    @Override
    public List<PostCommentInfoDTO> getChildComments(Long parentId) {
        return jpaQueryFactory.select(Projections.constructor(
                        PostCommentInfoDTO.class,
                        postComment.id,
                        postComment.createDate,
                        postComment.modifiedDate,
                        postComment.content,
                        Projections.constructor(
                                AuthorInfoDTO.class,
                                member.id,
                                member.userNickname,
                                member.userProfileImage
                        )
                ))
                .from(postComment)
                .leftJoin(postComment.author, member)
                .where(postComment.parentComment.id.eq(parentId))
                .orderBy(postComment.createDate.asc())
                .distinct()
                .fetch();
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


    private boolean getIsVotedPost(long postId, long memberId) {
        return jpaQueryFactory
                .select(vote)
                .from(vote)
                .where(vote.id.post.id.eq(postId)
                        .and(vote.id.voter.id.eq(memberId)))
                .fetchOne() != null;
    }

    private List<VoteInfoDTO> getVoteInfoList(long postId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(VoteInfoDTO.class,
                                vote.isAgree,
                                vote.grade,
                                vote.regionType,
                                vote.schoolType,
                                vote.gender
                        ))
                .from(vote)
                .where(vote.id.post.id.eq(postId))
                .fetch();
    }


}
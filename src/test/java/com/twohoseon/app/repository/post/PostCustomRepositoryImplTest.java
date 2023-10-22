package com.twohoseon.app.repository.post;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.AuthorInfoDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.dto.response.VoteInfoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QPost.post;
import static com.twohoseon.app.entity.post.vote.QVote.vote;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCustomRepositoryImplTest
 * @date : 10/22/23 12:54 AM
 * @modifyed : $
 **/
@SpringBootTest
class PostCustomRepositoryImplTest {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void getVoteInfoTest() {
        getVoteInfo(1);
    }

    @Test
    void getIsVotedPostTest() {
        System.out.println("getIsVotedPost(57, 1) = " + getIsVotedPost(57, 1));
        System.out.println("getIsVotedPost(58, 1) = " + getIsVotedPost(58, 1));
    }

    @Test
    void getVoteInfoListTest() {
        List<VoteInfoDTO> voteInfoList = getVoteInfoList(57);
        System.out.println("voteInfoList = " + voteInfoList);
    }

    @Test
    void getPostInfoTest() {
        PostInfoDTO infoDTO = findPostById(1, 1);
        System.out.println("infoDTO = " + infoDTO);
    }

    private VoteCountsDTO getVoteInfo(long postId) {
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
                                vote.schoolType,
                                vote.gender))
                .from(vote)
                .where(vote.id.post.id.eq(postId))
                .fetch();
    }

    private PostInfoDTO findPostById(long postId, long memberId) {
        PostInfoDTO postInfo = jpaQueryFactory
                .select(Projections.constructor(PostInfoDTO.class,
                        post.id.as("post_id"),
                        post.createDate,
                        post.modifiedDate,
                        post.postType,
                        post.postStatus,
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
                        post.commentCount
                ))
                .from(post)
                .where(post.id.eq(postId))
                .leftJoin(post.author, member)
                .leftJoin(post.votes, vote)
                .distinct().fetchOne();

        postInfo.setVoteCounts(getVoteInfo(postInfo.getPostId()));
        postInfo.setIsVoted(getIsVotedPost(postInfo.getPostId(), memberId));
        postInfo.setVoteInfoList(getVoteInfoList(postId));
        return postInfo;
    }

}
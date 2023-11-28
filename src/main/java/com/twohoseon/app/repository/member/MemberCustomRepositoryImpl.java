package com.twohoseon.app.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.mypage.BlockedMember;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.twohoseon.app.entity.member.QDeviceToken.deviceToken;
import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.member.QReport.report;
import static com.twohoseon.app.entity.post.QComment.comment;
import static com.twohoseon.app.entity.post.QPost.post;
import static com.twohoseon.app.entity.post.vote.QVote.vote;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberCustomRepositoryImpl
 * @date : 11/5/23 5:19 AM
 * @modifyed : $
 **/

@RequiredArgsConstructor
@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void detachVoteFromMember(Long id) {
        jpaQueryFactory.update(vote)
                .set(vote.voter, (Member) null)
                .where(vote.voter.id.eq(id))
                .execute();
    }

    @Override
    public void detachCommentsFromMember(Long memberId) {
        jpaQueryFactory.update(comment)
                .set(comment.author, (Member) null)
                .where(comment.author.id.eq(memberId))
                .execute();
    }

    @Override
    public void deleteSubscriptionsFromMember(Long memberId) {
        List<Post> posts = jpaQueryFactory.selectFrom(post)
                .where(post.subscribers.any().id.eq(memberId))
                .fetch();
        for (Post post : posts) {
            post.getSubscribers().removeIf(member -> member.getId().equals(memberId));
        }
//        postRepository.saveAll(posts);
    }

    @Override
    @Transactional
    public void deletePostsFromMember(Member author) {

        List<Long> postIds = jpaQueryFactory.select(post.id)
                .from(post)
                .where(post.author.id.eq(author.getId()))
                .fetch();
        List<Long> commentIds = jpaQueryFactory.select(comment.id)
                .from(comment)
                .where(comment.post.id.in(postIds).and(comment.parentComment.isNull()))
                .fetch();
        jpaQueryFactory.delete(comment)
                .where(comment.parentComment.id.in(commentIds))
                .execute();
        jpaQueryFactory.delete(comment)
                .where(comment.post.id.in(postIds))
                .execute();
        jpaQueryFactory.delete(vote)
                .where(vote.post.id.in(postIds))
                .execute();
        jpaQueryFactory.delete(post)
                .where(post.author.id.eq(author.getId())).execute();

//        for (Post post : posts) {
//            List<Comment> comments = jpaQueryFactory.selectFrom(comment)
//                    .where(comment.post.id.eq(post.getId()))
//                    .fetch();
//            commentRepository.deleteAll(comments);
//        }
//        postRepository.deleteByAuthor(author);
    }


    @Override
    public ProfileInfo getProfile(long memberId) {
        ProfileInfo profileInfo = jpaQueryFactory
                .select(Projections.constructor(ProfileInfo.class,
                        member.createDate,
                        member.modifiedDate,
                        member.lastSchoolRegisterDate,
                        member.nickname,
                        member.profileImage,
                        member.consumerType,
                        member.school))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        return profileInfo;
    }

    @Override
    public List<BlockedMember> getBlockedMembers(Member reqMember) {
        Set<Member> blockedMembers = reqMember.getBlockedMember();
        List<Long> blockedMemberIds = blockedMembers.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return jpaQueryFactory.select(
                        Projections.constructor(BlockedMember.class,
                                member.id,
                                member.nickname
                        ))
                .from(member)
                .where(member.id.in(blockedMemberIds))
                .fetch();
    }

    @Override
    public void deleteDeviceTokenFromMember(Long memberId) {
        jpaQueryFactory.delete(deviceToken)
                .where(deviceToken.member.id.eq(memberId))
                .execute();
    }

    @Override
    public void detachReportsFromMember(Long memberId) {
        jpaQueryFactory.update(report)
                .set(report.reporter, (Member) null)
                .where(report.reporter.id.eq(memberId))
                .execute();
    }


}

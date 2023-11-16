package com.twohoseon.app.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.twohoseon.app.entity.post.QComment.comment;
import static com.twohoseon.app.entity.post.QPost.post;
import static com.twohoseon.app.entity.post.vote.QVote.vote;

import static com.twohoseon.app.entity.member.QMember.member;

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
        List<Post> posts = jpaQueryFactory.selectFrom(post)
                .where(post.author.id.eq(author.getId()))
                .fetch();
        for (Post post : posts) {
            List<Comment> comments = jpaQueryFactory.selectFrom(comment)
                    .where(comment.post.id.eq(post.getId()))
                    .fetch();
            commentRepository.deleteAll(comments);
        }
        postRepository.deleteByAuthor(author);
    }

    @Override
    public void deletePostById(Long postId) {
        jpaQueryFactory.delete(comment)
                .where(comment.post.id.eq(postId))
                .execute();

        jpaQueryFactory.delete(post)
                .where(post.id.eq(postId))
                .execute();
    }


    @Override
    public ProfileInfo getProfile(long memberId) {
        ProfileInfo profileInfo = jpaQueryFactory
                .select(Projections.constructor(ProfileInfo.class,
                        member.id,
                        member.nickname,
                        member.profileImage,
                        member.consumerType,
                        member.school.schoolName))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        return profileInfo;
    }

}

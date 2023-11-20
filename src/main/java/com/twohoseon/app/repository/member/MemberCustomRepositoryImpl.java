package com.twohoseon.app.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
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
//        @Schema(name = "createDate", type = "LocalDateTime", description = "유저 생성일")
//        LocalDateTime createDate;
//        @Schema(name = "modifiedDate", type = "LocalDateTime", description = "유저 수정일")
//        LocalDateTime modifiedDate;
//        @Schema(name = "lastSchoolRegisterDate", type = "LocalDateTime", description = "유저 마지막 학교 등록일")
//        LocalDateTime lastSchoolRegisterDate;
//        @Schema(name = "nickname", type = "String", description = "유저 닉네임")
//        String nickname;
//        @Schema(name = "profileImage", type = "String", description = "유저 프로필 이미지")
//        String profileImage;
//        @Schema(name = "consumerType", type = "ConsumerType", description = "소비 성향")
//        ConsumerType consumerType;
//        @Schema(name = "schoolName", type = "String", description = "학교 이름")
//        String schoolName;
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

}

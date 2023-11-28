package com.twohoseon.app.repository.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.CommentInfo;
import com.twohoseon.app.dto.response.post.AuthorInfo;
import com.twohoseon.app.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QComment.comment;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentCustomRepositoryImpl
 * @date : 11/6/23 10:49 PM
 * @modifyed : $
 **/
@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentInfo> getAllCommentsFromPost(Long postId, Member reqMember) {
        StringExpression contentExpression = new CaseBuilder()
                .when(member.isBaned.isTrue())
                .then("")
                .otherwise(comment.content);
        List<CommentInfo> commentInfoList = jpaQueryFactory.select(Projections.constructor(
                        CommentInfo.class,
                        comment.id,
                        comment.createDate,
                        comment.modifiedDate,
                        contentExpression,
                        Projections.constructor(
                                AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType,
                                member.in(reqMember.getBlockedMember()),
                                member.isBaned
                        ),
                        comment.author.id.eq(reqMember.getId())

                ))
                .from(comment)
                .leftJoin(comment.author, member)
                .where(
                        comment.parentComment.isNull()
                                .and(comment.post.id.eq(postId))
                )
                .orderBy(comment.createDate.asc())
                .distinct()
                .fetch();

        for (CommentInfo comments : commentInfoList) {
            List<CommentInfo> subComments = getSubComments(comments.getCommentId(), reqMember);
            comments.setSubComments(subComments.isEmpty() ? null : subComments);
        }

        return commentInfoList;
    }

    @Override
    public List<CommentInfo> getSubComments(Long parentId, Member reqMember) {

        StringExpression contentExpression = new CaseBuilder()
                .when(member.isBaned.isTrue())
                .then("")
                .otherwise(comment.content);
        return jpaQueryFactory.select(Projections.constructor(
                        CommentInfo.class,
                        comment.id,
                        comment.createDate,
                        comment.modifiedDate,
                        contentExpression,
                        Projections.constructor(
                                AuthorInfo.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType,
                                member.in(reqMember.getBlockedMember()),
                                member.isBaned
                        ),
                        comment.author.id.eq(reqMember.getId())
                ))
                .from(comment)
                .leftJoin(comment.author, member)
                .where(comment.parentComment.id.eq(parentId))
                .orderBy(comment.createDate.asc())
                .distinct()
                .fetch();
    }

}

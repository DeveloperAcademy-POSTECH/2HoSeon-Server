package com.twohoseon.app.repository.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.AuthorInfoDTO;
import com.twohoseon.app.dto.response.CommentInfoDTO;
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
    public List<CommentInfoDTO> getAllCommentsFromPost(Long postId) {
        List<CommentInfoDTO> commentInfoList = jpaQueryFactory.select(Projections.constructor(
                        CommentInfoDTO.class,
                        comment.id,
                        comment.createDate,
                        comment.modifiedDate,
                        comment.content,
                        Projections.constructor(
                                AuthorInfoDTO.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType
                        )
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

        for (CommentInfoDTO comments : commentInfoList) {
            List<CommentInfoDTO> subComments = getSubComments(comments.getCommentId());
            comments.setSubComments(subComments.isEmpty() ? null : subComments);
        }

        return commentInfoList;
    }

    @Override
    public List<CommentInfoDTO> getSubComments(Long parentId) {
        return jpaQueryFactory.select(Projections.constructor(
                        CommentInfoDTO.class,
                        comment.id,
                        comment.createDate,
                        comment.modifiedDate,
                        comment.content,
                        Projections.constructor(
                                AuthorInfoDTO.class,
                                member.id,
                                member.nickname,
                                member.profileImage,
                                member.consumerType
                        )
                ))
                .from(comment)
                .leftJoin(comment.author, member)
                .where(comment.parentComment.id.eq(parentId))
                .orderBy(comment.createDate.asc())
                .distinct()
                .fetch();
    }
}

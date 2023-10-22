package com.twohoseon.app.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.AuthorInfoDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QPost.post;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchRepositoryImpl
 * @date : 2023/10/22
 * @modifyed : $
 **/

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostInfoDTO> findAllPostsByKeyword(Pageable pageable, String keyword) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return jpaQueryFactory
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
                        post.commentCount
                ))
                .from(post)
                .leftJoin(post.author, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(post.title.contains(keyword)
                        .or(post.contents.contains(keyword))
                        .or(post.postTagList.contains(keyword)))
                .fetch();
    }
}

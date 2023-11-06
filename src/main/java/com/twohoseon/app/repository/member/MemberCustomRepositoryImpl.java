package com.twohoseon.app.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QPost.post;

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

    @Override
    public List<String> findMemberDeviceTokenByPostId(Long postId) {
        return jpaQueryFactory
                .select(member.deviceToken)
                .from(post)
                .join(post.subscribers, member)
                .where(post.id.eq(postId))
                .fetch();
    }
}

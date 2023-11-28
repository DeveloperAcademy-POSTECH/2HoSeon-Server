package com.twohoseon.app.repository.device.token;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twohoseon.app.entity.member.QDeviceToken.deviceToken;
import static com.twohoseon.app.entity.member.QMember.member;
import static com.twohoseon.app.entity.post.QPost.post;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : DeviceTokenCustomRepositoryImpl
 * @date : 11/13/23 10:31 PM
 * @modifyed : $
 **/
@RequiredArgsConstructor
@Repository
public class DeviceTokenCustomRepositoryImpl implements DeviceTokenCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    //    QDeviceToken.
    @Override
    public List<String> findAllByMemberId(Long memberId) {

        return jpaQueryFactory
                .select(deviceToken.token)
                .from(deviceToken)
                .where(deviceToken.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<String> findAllTokensBySubscribers(Long postId) {
        return jpaQueryFactory
                .select(deviceToken.token)
                .from(deviceToken)
                .where(deviceToken.member.in(
                        jpaQueryFactory
                                .select(member)
                                .from(post)
                                .join(post.subscribers, member)
                                .where(post.id.eq(postId).and(member.isBaned.isFalse()))
                ))
                .fetch();
    }


}

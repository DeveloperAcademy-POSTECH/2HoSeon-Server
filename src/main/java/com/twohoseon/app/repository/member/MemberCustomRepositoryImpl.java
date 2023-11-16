package com.twohoseon.app.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

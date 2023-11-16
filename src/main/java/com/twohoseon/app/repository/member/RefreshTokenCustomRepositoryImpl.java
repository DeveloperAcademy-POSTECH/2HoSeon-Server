package com.twohoseon.app.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.twohoseon.app.entity.member.QRefreshToken.refreshToken1;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshTokenCustomRepositoryImpl
 * @date : 11/15/23 12:36 AM
 * @modifyed : $
 **/
@Repository
@RequiredArgsConstructor
public class RefreshTokenCustomRepositoryImpl implements RefreshTokenCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByAccessTokenAndIsBannedTrue(String accessToken) {
        return jpaQueryFactory.selectOne()
                .from(refreshToken1)
                .where(refreshToken1.isBanned.isTrue())
                .fetchFirst() != null;
    }
}

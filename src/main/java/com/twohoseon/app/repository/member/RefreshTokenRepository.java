package com.twohoseon.app.repository.member;

import com.twohoseon.app.entity.member.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshTokenRepository
 * @date : 2023/10/07 3:51 PM
 * @modifyed : $
 **/
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenCustomRepository {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    boolean existsByAccessTokenAndIsBannedTrue(String accessToken);

}

package com.twohoseon.app.repository.member;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshTokenCustomRepository
 * @date : 11/15/23 12:35 AM
 * @modifyed : $
 **/

public interface RefreshTokenCustomRepository {
    boolean existsByAccessTokenAndIsBannedTrue(String accessToken);

}

package com.twohoseon.app.repository.device.token;

import com.twohoseon.app.entity.member.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : DeviceTokenRepository
 * @date : 11/13/23 9:11 PM
 * @modifyed : $
 **/
@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long>, DeviceTokenCustomRepository {


    Optional<DeviceToken> findByToken(String deviceToken);


}

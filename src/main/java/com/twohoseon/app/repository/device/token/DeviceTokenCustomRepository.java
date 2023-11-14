package com.twohoseon.app.repository.device.token;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : DeviceTokenCustomRepository
 * @date : 11/13/23 10:30 PM
 * @modifyed : $
 **/
public interface DeviceTokenCustomRepository {
    List<String> findAllByMemberId(Long memberId);

    List<String> findAllByPostId(Long id);
}

package com.twohoseon.app.repository.member;

import com.twohoseon.app.dto.response.profile.ProfileInfo;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberCustomRepository
 * @date : 11/5/23 5:18 AM
 * @modifyed : $
 **/
public interface MemberCustomRepository {

    ProfileInfo getProfile(long memberId);
}

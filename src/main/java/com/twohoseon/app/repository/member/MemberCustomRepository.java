package com.twohoseon.app.repository.member;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberCustomRepository
 * @date : 11/5/23 5:18 AM
 * @modifyed : $
 **/
public interface MemberCustomRepository {
    List<String> findMemberDeviceTokenByPostId(Long postId);
}

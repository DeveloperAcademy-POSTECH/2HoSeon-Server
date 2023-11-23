package com.twohoseon.app.repository.member;

import com.twohoseon.app.dto.response.mypage.BlockedMember;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import com.twohoseon.app.entity.member.Member;

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
    void detachVoteFromMember(Long memberId);

    void detachCommentsFromMember(Long memberId);

    void deleteSubscriptionsFromMember(Long memberId);

    void deletePostsFromMember(Member memberId);


    ProfileInfo getProfile(long memberId);

    List<BlockedMember> getBlockedMembers(Long id);

}

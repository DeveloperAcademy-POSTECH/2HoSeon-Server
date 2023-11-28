package com.twohoseon.app.repository.member;

import com.twohoseon.app.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberRepository
 * @date : 2023/10/07 1:48 PM
 * @modifyed : $
 **/
//@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    boolean existsByNickname(String userNickname);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ADMIN.MEMBER_BLOCK WHERE BLOCKER_ID = ?1 OR BLOCKED_ID = ?1", nativeQuery = true)
    void deleteMemberBlockFromMember(Long memberId);

//    void deleteMemberBlockFromMember(Member reqMember);
}

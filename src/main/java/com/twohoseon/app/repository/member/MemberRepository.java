package com.twohoseon.app.repository.member;

import com.twohoseon.app.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberRepository
 * @date : 2023/10/07 1:48 PM
 * @modifyed : $
 **/
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    Optional<Member> findByUserNickname(String userNickname);
}

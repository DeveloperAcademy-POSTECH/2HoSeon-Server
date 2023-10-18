package com.twohoseon.app.service.member;

import com.twohoseon.app.dto.ProfileRequestDTO;
import com.twohoseon.app.entity.Member;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberServiceImpl
 * @date : 2023/10/07 4:20 PM
 * @modifyed : $
 **/
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberRepository.findByProviderId(providerId);
        if (memberOptional.isPresent()) {
            return new MemberDetails(memberOptional.get());
        }
        //TODO member not found exception handling
        return null;
    }

    @Override
    public void setUserProfile(ProfileRequestDTO profileRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();
        //TODO Exception Handler 추가되면 오류 발생시 Exception 발생시키기

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("wrong access"));

        member.updateAdditionalUserInfo(profileRequestDTO.getUserProfileImage(),
                profileRequestDTO.getUserNickname(),
                profileRequestDTO.getSchool(),
                profileRequestDTO.getGrade());

        memberRepository.save(member);
    }

    @Override
    public boolean validateDuplicateUserNickname(String userNickname) {
        return memberRepository.existsByUserNickname(userNickname);
    }

    public Optional<Member> findByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }
}
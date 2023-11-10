package com.twohoseon.app.service.member;

import com.twohoseon.app.dto.request.member.ProfileRequestDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.MemberDetails;
import com.twohoseon.app.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberServiceImpl
 * @date : 2023/10/07 4:20 PM
 * @modifyed : $
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with providerId : " + providerId));
        return new MemberDetails(member);
    }

    @Override
    @Transactional
    public void setUserProfile(ProfileRequestDTO profileRequestDTO, MultipartFile imageFile) {

        Member member = getMemberFromRequest();
        log.debug("profileRequestDTO = " + profileRequestDTO.toString());

        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {

            if (member.getProfileImage() == null) {
                imageName = imageService.uploadImage(imageFile, "profiles");
            } else {
                imageService.deleteImage(member.getProfileImage().toString(), "profiles");
                imageName = imageService.uploadImage(imageFile, "profiles");
            }
        }

        member.updateAdditionalUserInfo(
                imageName,
                profileRequestDTO.getNickname(),
                profileRequestDTO.getSchool()
        );
        memberRepository.save(member);
    }

    @Override
    public boolean validateDuplicateUserNickname(String userNickname) {
        return memberRepository.existsByNickname(userNickname);
    }

    public Optional<Member> findByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }

    @Override
    public void registerToken(String deviceToken) {
        Member member = getMemberFromRequest();
        member.updateDeviceToken(deviceToken);
    }
}
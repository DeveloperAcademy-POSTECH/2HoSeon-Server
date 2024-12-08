package com.twohoseon.app.service.member;

import com.twohoseon.app.dto.ConsumerTypeRequest;
import com.twohoseon.app.dto.request.member.ProfileRequest;
import com.twohoseon.app.dto.response.mypage.BlockedMember;
import com.twohoseon.app.dto.response.profile.ProfileInfo;
import com.twohoseon.app.entity.member.DeviceToken;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.exception.MemberNotFoundException;
import com.twohoseon.app.exception.SchoolUpdateRestrictionException;
import com.twohoseon.app.repository.banlist.BanListRepository;
import com.twohoseon.app.repository.device.token.DeviceTokenRepository;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.MemberDetails;
import com.twohoseon.app.service.image.ImageService;
import com.twohoseon.app.service.notification.NotificationService;
import com.twohoseon.app.util.AppleUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private final BanListRepository banListRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationService notificationService;
    private final AppleUtility appleUtility;

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with providerId : " + providerId));
        return new MemberDetails(member);
    }

    @Override
    @Transactional
    public void setUserProfile(ProfileRequest profileRequest, MultipartFile imageFile) {

        Member member = getMemberFromRequest();
        log.debug("profileRequestDTO = " + profileRequest.toString());

        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {

            if (member.getProfileImage() == null) {
                imageName = imageService.uploadImage(imageFile, "profiles");
            } else {
                imageService.deleteImage(member.getProfileImage().toString(), "profiles");
                imageName = imageService.uploadImage(imageFile, "profiles");
            }
        }
        if (profileRequest.hasSchool()) {
            if (!member.isSchoolRegistrable()) {
                throw new SchoolUpdateRestrictionException();
            }
        }
        member.updateAdditionalUserInfo(
                imageName,
                profileRequest.getNickname(),
                profileRequest.getSchool()
        );
        if (banListRepository.existsByProviderId(member.getProviderId())) {
            member.setIsBaned(true);
        }
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
        DeviceToken deviceTokenEntity = deviceTokenRepository.findByToken(deviceToken)
                .orElse(DeviceToken.builder()
                        .token(deviceToken)
                        .build());
        deviceTokenEntity.setMember(member);
        deviceTokenRepository.save(deviceTokenEntity);
    }

    @Override
    public void setConsumptionTendency(ConsumerTypeRequest consumptionTendencyRequestDTO) {
        Member reqMember = getMemberFromRequest();
        reqMember.updateConsumerType(consumptionTendencyRequestDTO.getConsumerType());
        memberRepository.save(reqMember);
        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendConsumerTypeNotification(reqMember);
            } catch (ExecutionException | InterruptedException e) {
                log.debug("sendConsumerTypeNotification error: ", e);
            }
        });
    }

    @Override
    @Transactional
    public void deleteMember() {
        Member reqMember = getMemberFromRequest();
        deleteMember(reqMember);
        CompletableFuture.runAsync(() -> appleUtility.revokeAppleToken(reqMember.getAppleRefreshToken()));
    }

    @Override
    @Transactional
    public void deleteAppleMember(String providerId) {
        Member reqMember = getMemberFromRequest();
        deleteMember(reqMember);
    }

    @Override
    public ProfileInfo getProfile() {
        Member member = getMemberFromRequest();
        return memberRepository.getProfile(member.getId());
    }

    @Override
    @Transactional
    public void blockMember(Long memberId) {
        Member reqMember = getMemberFromRequest();
        Member blockMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        reqMember.blockedMember(blockMember);
        memberRepository.save(reqMember);
    }

    @Override
    public void unblockMember(Long memberId) {
        Member reqMember = getMemberFromRequest();
        Member blockedMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        reqMember.unBlockedMember(blockedMember);
        memberRepository.save(reqMember);
    }

    @Override
    public List<BlockedMember> getBlockedMembers() {
        Member reqMember = getMemberFromRequest();
        log.debug(String.valueOf(reqMember.getId()));
        List<BlockedMember> blockedMembers = memberRepository.getBlockedMembers(reqMember);
        return blockedMembers;
    }

    @Override
    @Transactional
    public void detachVoteFromMember() {
        Member reqMember = getMemberFromRequest();
        memberRepository.detachVoteFromMember(reqMember.getId());
    }


    private void deleteMember(Member reqMember) {
        memberRepository.detachVoteFromMember(reqMember.getId());
        memberRepository.deleteSubscriptionsFromMember(reqMember.getId());
        memberRepository.detachCommentsFromMember(reqMember.getId());
        memberRepository.detachReportsFromMember(reqMember.getId());
        memberRepository.deletePostsFromMember(reqMember);
        memberRepository.deleteMemberBlockFromMember(reqMember.getId());
        memberRepository.deleteDeviceTokenFromMember(reqMember.getId());
        memberRepository.banJWTTokenFromMember(reqMember);
        memberRepository.delete(reqMember);
    }
}
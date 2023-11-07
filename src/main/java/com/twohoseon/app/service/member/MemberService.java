package com.twohoseon.app.service.member;

import com.twohoseon.app.dto.request.member.ProfileRequestDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.service.CommonService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberService
 * @date : 2023/10/07 4:20 PM
 * @modifyed : $
 **/
public interface MemberService extends UserDetailsService, CommonService {
    void setUserProfile(ProfileRequestDTO profileRequestDTO, MultipartFile imageFile);

    boolean validateDuplicateUserNickname(String userNickname);

    Optional<Member> findByProviderId(String providerId);

    void registerToken(String deviceToken);
}

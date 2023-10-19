package com.twohoseon.app.service.oauth2;

import com.twohoseon.app.entity.Member;
import com.twohoseon.app.enums.OAuth2Provider;
import com.twohoseon.app.enums.UserRole;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.security.oauth2.userinfo.CustomOAuth2UserInfo;
import com.twohoseon.app.security.oauth2.userinfo.CustomOAuth2UserInfoFactory;

import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomOAuth2UserService
 * @date : 2023/10/07 1:43 PM
 * @modifyed : $
 **/
public interface CustomOAuth2Service {
    default Member processUser(MemberRepository memberRepository, OAuth2Provider provider,
                               Map<String, Object> attributes) {
        System.out.println("attributes = " + attributes);
        CustomOAuth2UserInfo userInfo = CustomOAuth2UserInfoFactory.create(provider, attributes);
        String providerId = userInfo.getProviderId();
        String userName = userInfo.getUserName();
        String userEmail = userInfo.getUserEmail();
        System.out.println("userEmail = " + userEmail);
        System.out.println("userName = " + userName);
        return memberRepository.findByProviderId(providerId)
                .orElseGet(() -> {
                    Member member = Member.builder()
                            .provider(provider)
                            .providerId(providerId)
                            .userEmail(userEmail)
                            .userName(userName)
                            .role(UserRole.ROLE_USER)
                            .build();
                    System.out.println("member = " + member);
                    return memberRepository.save(member);
                });
    }
}

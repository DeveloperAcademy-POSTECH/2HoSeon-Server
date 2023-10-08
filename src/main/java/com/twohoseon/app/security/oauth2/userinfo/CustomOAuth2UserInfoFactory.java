package com.twohoseon.app.security.oauth2.userinfo;

import com.twohoseon.app.enums.OAuth2Provider;

import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomOAuth2UserInfoFactory
 * @date : 2023/10/07 2:47 PM
 * @modifyed : $
 **/
public class CustomOAuth2UserInfoFactory {
    public static CustomOAuth2UserInfo create(OAuth2Provider provider,
                                              Map<String, Object> attributes) {
        return switch (provider) {
            case APPLE -> new AppleOAuth2UserInfo(attributes);
        };
    }
}

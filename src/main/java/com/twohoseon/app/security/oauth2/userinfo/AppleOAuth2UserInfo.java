package com.twohoseon.app.security.oauth2.userinfo;

import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleOAuth2UserInfo
 * @date : 2023/10/07 3:13 PM
 * @modifyed : $
 **/
public class AppleOAuth2UserInfo implements CustomOAuth2UserInfo{
    private final Map<String, Object> attributes;

    public AppleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getUserEmail() {
        return null;
    }

    @Override
    public String getUserName() {
        return null;
    }
}

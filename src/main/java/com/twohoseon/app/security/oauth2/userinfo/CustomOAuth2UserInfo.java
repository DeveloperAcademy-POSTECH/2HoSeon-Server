package com.twohoseon.app.security.oauth2.userinfo;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomOAuth2UserInfo
 * @date : 2023/10/07 2:47 PM
 * @modifyed : $
 **/
public interface CustomOAuth2UserInfo {
    String getProviderId();
    String getUserEmail();
    String getUserName();
}

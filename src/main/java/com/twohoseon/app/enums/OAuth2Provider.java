package com.twohoseon.app.enums;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : OAuth2Provider
 * @date : 2023/10/07 1:32 PM
 * @modifyed : $
 **/
public enum OAuth2Provider {
    APPLE;
    public static OAuth2Provider getProvider(OAuth2UserRequest request){
        String registrationId = request.getClientRegistration().getRegistrationId();
        return OAuth2Provider.valueOf(registrationId.toUpperCase());
    }

}

package com.twohoseon.app.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommonService
 * @date : 10/19/23 11:01 AM
 * @modifyed : $
 **/
public interface CommonService {
    default String getProviderIdFromRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();
        return providerId;
    }
}

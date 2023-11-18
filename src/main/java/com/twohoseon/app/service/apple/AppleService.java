package com.twohoseon.app.service.apple;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : appleService
 * @date : 11/18/23 12:32 AM
 * @modifyed : $
 **/
public interface AppleService {
    void revokeAppleToken(String appleRefreshToken);

    void listenDeleteMemberEvent(String payload);

}

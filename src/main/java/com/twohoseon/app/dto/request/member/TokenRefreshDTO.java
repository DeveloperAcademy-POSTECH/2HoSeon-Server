package com.twohoseon.app.dto.request.member;

import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : TokenRefreshDTO
 * @date : 10/16/23 3:30 PM
 * @modifyed : $
 **/
@Getter
public class TokenRefreshDTO {
    String refreshToken;
    String identifier;
}

package com.twohoseon.app.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "TokenRefreshDTO", description = "토큰 재발급 요청 DTO")
public class TokenRefresh {
    @Schema(name = "refreshToken", type = "String", description = "리프레시 토큰")
    String refreshToken;
}

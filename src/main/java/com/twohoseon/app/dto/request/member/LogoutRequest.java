package com.twohoseon.app.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : LogoutRequest
 * @date : 11/28/23 2:35 AM
 * @modifyed : $
 **/
@Schema(name = "LogoutRequest", description = "로그아웃 요청")
@Getter
public class LogoutRequest {
    @Schema(name = "deviceToken", description = "디바이스 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
    String deviceToken;
}

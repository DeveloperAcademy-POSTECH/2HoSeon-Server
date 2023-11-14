package com.twohoseon.app.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RegisterDeviceTokenRequestDTO
 * @date : 10/31/23 9:56 PM
 * @modifyed : $
 **/
@Getter
@Schema(name = "RegisterDeviceTokenRequestDTO", description = "디바이스 토큰 등록 요청")
public class RegisterDeviceTokenRequest {
    @Schema(name = "deviceToken", type = "String", description = "디바이스 토큰")
    private String deviceToken;
}

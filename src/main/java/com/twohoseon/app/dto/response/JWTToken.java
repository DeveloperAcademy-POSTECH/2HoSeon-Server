package com.twohoseon.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : TokenDTO
 * @date : 2023/10/07 6:20 PM
 * @modifyed : $
 **/
@Builder
@Getter
@Schema(name = "TokenDTO", description = "토큰 응답 DTO")
public class JWTToken {
    @Schema(name = "accessToken", type = "String", description = "액세스 토큰")
    private String accessToken;
    @Schema(name = "accessExpirationTime", type = "long", description = "액세스 토큰 만료 시간")
    private long accessExpirationTime;
    @Schema(name = "refreshToken", type = "String", description = "리프레시 토큰")
    private String refreshToken;
    @Schema(name = "refreshExpirationTime", type = "long", description = "리프레시 토큰 만료 시간")
    private long refreshExpirationTime;
}

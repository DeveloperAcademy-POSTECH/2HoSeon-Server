package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : LoginInfo
 * @date : 11/17/23 5:30 PM
 * @modifyed : $
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Schema(name = "LoginInfo", description = "로그인 정보 DTO")
public class LoginInfo {
    @Schema(name = "consumerTypeExist", type = "Boolean", description = "소비 성향 테스트 여부")
    private Boolean consumerTypeExist;
    @Schema(name = "jwtToken", type = "JWTToken", description = "JWT 토큰")
    private JWTToken jwtToken;
}

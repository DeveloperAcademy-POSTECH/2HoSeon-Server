package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AuthorInfoResponse
 * @date : 2023/11/16
 * @modifyed : $
 **/

@Data
@Schema(name = "LoginResponse", description = "프로필 보기 DTO")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    @Schema(name = "status", type = "int", description = "응답 상태")
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;

    @Schema(name = "message", type = "String", description = "응답 메시지")
    private String message;

    @Schema(name = "data", type = "LoginInfo", description = "응답 데이터")
    private LoginInfo data;
}

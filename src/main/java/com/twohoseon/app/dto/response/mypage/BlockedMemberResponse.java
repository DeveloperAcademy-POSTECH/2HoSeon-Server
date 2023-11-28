package com.twohoseon.app.dto.response.mypage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : BlockedMemberResponse
 * @date : 11/23/23 3:45 PM
 * @modifyed : $
 **/
@Data
@Schema(name = "BlockedMemberResponse", description = "차단된 유저 조회 응답")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockedMemberResponse {
    @Schema(name = "status", type = "int", description = "응답 상태")
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;

    @Schema(name = "message", type = "String", description = "응답 메시지")
    private String message;

    @Schema(name = "data", type = "BlockedMember", description = "응답 데이터")
    private List<BlockedMember> data;
}

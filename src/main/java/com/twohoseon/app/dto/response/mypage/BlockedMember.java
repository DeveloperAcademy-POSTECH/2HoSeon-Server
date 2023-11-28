package com.twohoseon.app.dto.response.mypage;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : BlockedMember
 * @date : 11/23/23 3:46 PM
 * @modifyed : $
 **/
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "BlockedMember", description = "차단된 유저 조회 응답")
public class BlockedMember {
    @Schema
    private Long id;
    @Schema
    private String nickname;
}

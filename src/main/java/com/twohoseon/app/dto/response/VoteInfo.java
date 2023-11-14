package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteInfoDTO
 * @date : 10/20/23 4:41 AM
 * @modifyed : $
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "VoteInfoDTO", description = "투표 정보 DTO")
@AllArgsConstructor
public class VoteInfo {
    @Schema(name = "isAgree", type = "boolean", description = "찬성 여부")
    private Boolean isAgree;
    @Schema(name = "consumerType", type = "ConsumerType", description = "소비자 타입")
    private ConsumerType consumerType;
}

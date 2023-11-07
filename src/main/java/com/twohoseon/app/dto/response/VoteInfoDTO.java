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
public class VoteInfoDTO {
    //TODO 투표를 했는가 like voted
    boolean isAgree;
    ConsumerType consumerType;
}

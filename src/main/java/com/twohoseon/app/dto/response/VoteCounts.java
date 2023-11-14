package com.twohoseon.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteCountsDTO
 * @date : 10/22/23 4:49 AM
 * @modifyed : $
 **/
@Getter
@AllArgsConstructor
@Schema(name = "VoteCountsDTO", description = "투표 수 DTO")
public class VoteCounts {
    @Schema(name = "agreeCount", type = "int", description = "찬성 수")
    int agreeCount;
    @Schema(name = "disagreeCount", type = "int", description = "반대 수")
    int disagreeCount;
}

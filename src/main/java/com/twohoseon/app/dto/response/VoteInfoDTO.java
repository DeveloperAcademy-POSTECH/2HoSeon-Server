package com.twohoseon.app.dto.response;

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
@Schema(name = "VoteInfoDTO", description = "투표 정보 DTO")
@AllArgsConstructor
public class VoteInfoDTO {
    Long agreeCount;
    Long disagreeCount;
}

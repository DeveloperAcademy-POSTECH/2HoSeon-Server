package com.twohoseon.app.dto.request.post;

import com.twohoseon.app.enums.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteCreateRequestDTO
 * @date : 10/20/23 3:17 AM
 * @modifyed : $
 **/
@Getter
@Schema(name = "VoteCreateRequestDTO", description = "투표 생성 요청 DTO")
public class VoteCreateRequestDTO {
    private VoteType voteType;
}

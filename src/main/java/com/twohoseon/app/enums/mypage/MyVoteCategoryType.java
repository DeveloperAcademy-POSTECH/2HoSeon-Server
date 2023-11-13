package com.twohoseon.app.enums.mypage;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MyVoteCategoryType
 * @date : 11/13/23 5:45 AM
 * @modifyed : $
 **/

@Schema(name = "MyVoteCategoryType", description = "마이페이지 투표 카테고리 타입")
public enum MyVoteCategoryType {
    @Schema(name = "ALL_VOTES", description = "전체 투표")
    ALL_VOTES,
    @Schema(name = "GLOBAL_VOTES", description = "전체 학교 투표")
    GLOBAL_VOTES,
    @Schema(name = "SCHOOL_VOTES", description = "내 학교 투표")
    SCHOOL_VOTES,
    @Schema(name = "ACTIVE_VOTES", description = "진행중인 투표")
    ACTIVE_VOTES,
    @Schema(name = "FINISHED_VOTES", description = "종료된 투표")
    FINISHED_VOTES;
}

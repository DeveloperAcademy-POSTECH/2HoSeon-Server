package com.twohoseon.app.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NicknameValidCheckRequest
 * @date : 10/21/23 9:24 PM
 * @modifyed : $
 **/
@Getter
@Schema(name = "NicknameValidCheckRequest", description = "닉네임 중복 확인 요청 DTO")
public class NicknameValidCheckRequest {
    @Schema(name = "nickname", type = "String", description = "닉네임")
    private String nickname;
}

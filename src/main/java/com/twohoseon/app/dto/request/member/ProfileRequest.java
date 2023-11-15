package com.twohoseon.app.dto.request.member;

import com.twohoseon.app.entity.member.School;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "ProfileRequestDTO", description = "프로필 수정 요청 DTO")
public class ProfileRequest {
    @Schema(name = "nickname", description = "유저 닉네임")
    private String nickname;
    @Schema(name = "school", description = "유저 학교")
    private School school;

    public boolean hasSchool() {
        return this.school != null;
    }
}

package com.twohoseon.app.dto.request;

import com.twohoseon.app.entity.member.School;
import com.twohoseon.app.enums.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "ProfileRequestDTO", description = "프로필 수정 요청 DTO")
public class ProfileRequestDTO {
    @Schema(name = "userProfileImage", description = "유저 프로필 이미지")
    private String userProfileImage;
    @Schema(name = "userNickname", description = "유저 닉네임")
    private String userNickname;
    @Schema(name = "userGender", description = "유저 성별")
    private GenderType userGender;
    @Schema(name = "school", description = "유저 학교")
    private School school;
    @Schema(name = "grade", description = "유저 학년")
    private int grade;
}

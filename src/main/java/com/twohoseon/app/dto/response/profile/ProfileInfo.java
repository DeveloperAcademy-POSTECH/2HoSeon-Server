package com.twohoseon.app.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.common.ImageDTO;
import com.twohoseon.app.entity.member.School;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ProfileInfo
 * @date : 2023/11/16
 * @modifyed : $
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ProfileInfo", description = "프로필 정보 DTO")
public class ProfileInfo extends ImageDTO {
    @Schema(name = "createDate", type = "LocalDateTime", description = "유저 생성일")
    LocalDateTime createDate;
    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "유저 수정일")
    LocalDateTime modifiedDate;
    @Schema(name = "lastSchoolRegisterDate", type = "LocalDateTime", description = "유저 마지막 학교 등록일")
    LocalDate lastSchoolRegisterDate;
    @Schema(name = "nickname", type = "String", description = "유저 닉네임")
    String nickname;
    @Schema(name = "profileImage", type = "String", description = "유저 프로필 이미지")
    String profileImage;
    @Schema(name = "consumerType", type = "ConsumerType", description = "소비 성향")
    ConsumerType consumerType;
    @Schema(name = "school", type = "School", description = "학교 정보")
    School school;
    @Schema(name = "canUpdateConsumerType", type = "boolean", description = "소비 성향 변경 가능 여부")
    boolean canUpdateConsumerType;

    public ProfileInfo(LocalDateTime createDate, LocalDateTime modifiedDate, LocalDate lastSchoolRegisterDate, String nickname, String profileImage, ConsumerType consumerType, School school, int canUpdateConsumerType) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.lastSchoolRegisterDate = lastSchoolRegisterDate;
        this.nickname = nickname;
        this.profileImage = generateProfileImageURL(profileImage);
        this.consumerType = consumerType;
        this.school = school;
        this.canUpdateConsumerType = canUpdateConsumerType < 2;
    }

}

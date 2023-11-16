package com.twohoseon.app.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.common.ImageDTO;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    @Schema(name = "id", type = "long", description = "유저 ID")
    Long id;
    @Schema(name = "nickname", type = "String", description = "유저 닉네임")
    String nickname;
    @Schema(name = "profileImage", type = "String", description = "유저 프로필 이미지")
    String profileImage;
    @Schema(name = "consumerType", type = "ConsumerType", description = "소비 성향")
    ConsumerType consumerType;
    @Schema(name = "schoolName", type = "String", description = "학교 이름")
    String schoolName;

    public ProfileInfo(long id, String nickname, String profileImage, ConsumerType consumerType, String schoolName) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = generateProfileImageURL(profileImage);
        this.consumerType = consumerType;
        this.schoolName = schoolName;
    }
}

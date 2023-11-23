package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.common.ImageDTO;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AuthorInfoDTO", description = "게시글 작성자 정보 DTO")
public class AuthorInfo extends ImageDTO {
    @Schema(name = "id", type = "long", description = "유저 ID")
    Long id;
    @Schema(name = "nickname", type = "String", description = "유저 닉네임")
    String nickname;
    @Schema(name = "profileImage", type = "String", description = "유저 프로필 이미지")
    String profileImage;
    @Schema(name = "consumerType", type = "ConsumerType", description = "소비 성향")
    ConsumerType consumerType;
    @Schema(name = "isBlocked", type = "Boolean", description = "차단 여부")
    Boolean isBlocked;

    public AuthorInfo(long id, String nickname, String profileImage, ConsumerType consumerType) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = generateProfileImageURL(profileImage);
        this.consumerType = consumerType;
    }

    public AuthorInfo(long id, String nickname, String profileImage, ConsumerType consumerType, Boolean isBlocked) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = generateProfileImageURL(profileImage);
        this.consumerType = consumerType;
        this.isBlocked = isBlocked;
    }
}
package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AuthorInfoDTO", description = "게시글 작성자 정보 DTO")
public class AuthorInfoDTO {
    @Schema(name = "id", type = "long", description = "유저 ID")
    long id;
    @Schema(name = "nickname", type = "String", description = "유저 닉네임")
    String nickname;
    @Schema(name = "profileImage", type = "String", description = "유저 프로필 이미지")
    String profileImage;
    @Schema(name = "consumerType", type = "ConsumerType", description = "소비 성향")
    ConsumerType consumerType;
}
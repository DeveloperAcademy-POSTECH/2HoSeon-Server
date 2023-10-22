package com.twohoseon.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "AuthorInfoDTO", description = "게시글 작성자 정보 DTO")
public class AuthorInfoDTO {
    @Schema(name = "id", type = "long", description = "유저 ID")
    long id;
    @Schema(name = "userNickname", type = "String", description = "유저 닉네임")
    String userNickname;
    @Schema(name = "userProfileImage", type = "String", description = "유저 프로필 이미지")
    String userProfileImage;
}
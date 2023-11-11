package com.twohoseon.app.dto.request.post;

import com.twohoseon.app.enums.post.VisibilityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCreateRequestDTO
 * @date : 10/18/23 11:21 PM
 * @modifyed : $
 **/
@Getter
@Schema(name = "PostRequestDTO", description = "게시글 생성 요청 DTO")
public class PostRequestDTO {
    @Schema(name = "visibilityScope", type = "VisibilityScope", description = "게시글 공개 범위")
    private VisibilityScope visibilityScope;
    @Schema(name = "title", type = "String", description = "게시글 제목")
    private String title;
    @Schema(name = "contents", type = "String", description = "게시글 내용")
    private String contents;
    @Schema(name = "price", type = "Integer", description = "가격")
    private Integer price;
    @Schema(name = "externalURL", type = "String", description = "게시글 외부 URL")
    private String externalURL;
}

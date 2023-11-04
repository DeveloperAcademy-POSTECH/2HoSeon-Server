package com.twohoseon.app.dto.request.post;

import com.twohoseon.app.enums.post.VisibilityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostUpdateRequestDTO
 * @date : 11/5/23 12:46 AM
 * @modifyed : $
 **/
@Schema(name = "PostUpdateRequestDTO", description = "게시글 수정 요청 DTO")
@Getter
public class PostUpdateRequestDTO {
    //TODO PostCreateRequestDTO와 동일하므로 추후에 하나로 합칠 것
    @Schema(name = "VisibilityScope", description = "게시글 공개 범위")
    private VisibilityScope visibilityScope;
    @Schema(name = "title", description = "게시글 제목")
    private String title;
    @Schema(name = "contents", description = "게시글 내용")
    private String contents;
    @Schema(name = "image", description = "게시글 이미지, 아마 multipart로 받아야 할듯")
    private String image;
    @Schema(name = "externalURL", description = "게시글 외부 URL")
    private String externalURL;
}

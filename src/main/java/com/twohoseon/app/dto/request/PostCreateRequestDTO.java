package com.twohoseon.app.dto.request;

import com.twohoseon.app.enums.post.VisibilityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCreateRequestDTO
 * @date : 10/18/23 11:21 PM
 * @modifyed : $
 **/
@Getter
@Schema(name = "PostCreateRequestDTO", description = "게시글 생성 요청 DTO")
public class PostCreateRequestDTO {
    @Schema(name = "postType", description = "게시글 타입")
    private VisibilityScope visibilityScope;
    @Schema(name = "title", description = "게시글 제목")
    private String title;
    @Schema(name = "contents", description = "게시글 내용")
    private String contents;
    @Schema(name = "image", description = "게시글 이미지, 아마 multipart로 받아야 할듯")
    private String image;
    @Schema(name = "externalURL", description = "게시글 외부 URL")
    private String externalURL;
    @Schema(name = "postTagList", description = "게시글 태그 리스트")
    private List<String> postTagList;
}

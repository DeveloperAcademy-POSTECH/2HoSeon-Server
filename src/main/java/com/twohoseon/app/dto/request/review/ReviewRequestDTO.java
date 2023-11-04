package com.twohoseon.app.dto.request.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewCreateRequestDTO
 * @date : 11/5/23 2:36 AM
 * @modifyed : $
 **/
@Schema(name = "ReviewCreateRequestDTO", description = "후기 생성 요청 DTO")
@Getter
public class ReviewRequestDTO {
    @Schema(name = "title", description = "게시글 제목")
    private String title;
    @Schema(name = "contents", description = "게시글 내용")
    private String contents;
    @Schema(name = "image", description = "게시글 이미지, 아마 multipart로 받아야 할듯")
    private String image;

}

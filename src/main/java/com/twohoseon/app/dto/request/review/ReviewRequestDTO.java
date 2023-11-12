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
@Schema(name = "ReviewRequestDTO", description = "후기 생성 요청 DTO")
@Getter
public class ReviewRequestDTO {
    @Schema(name = "title", type = "String", description = "후기 제목")
    private String title;
    @Schema(name = "contents", type = "String", description = "후기 내용")
    private String contents;
    @Schema(name = "price", type = "Integer", description = "제품 가격")
    private Integer price;
    @Schema(name = "isPurchased", type = "Boolean", description = "구매 여부")
    private Boolean isPurchased;
}

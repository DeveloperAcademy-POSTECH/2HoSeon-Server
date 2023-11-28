package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.enums.ReviewType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewFetchInfo
 * @date : 11/8/23 5:57 AM
 * @modifyed : $
 **/
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ReviewFetch", description = "리뷰 fetch 정보")
public class ReviewFetch {
    @Schema(name = "reviewType", type = "ReviewType", description = "리뷰 타입")
    ReviewType reviewType;
    @Schema(name = "reviews", type = "List<PostSummary>", description = "리뷰 리스트")
    List<PostSummary> reviews;
}

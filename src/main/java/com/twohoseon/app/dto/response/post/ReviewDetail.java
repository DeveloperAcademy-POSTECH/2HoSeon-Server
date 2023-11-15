package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.dto.response.PostInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewDetail
 * @date : 11/9/23 4:58 PM
 * @modifyed : $
 **/
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ReviewDetail", description = "리뷰 fetch 정보")
public class ReviewDetail {
    @Schema(name = "originalPost", type = "PostSummary", description = "원본 게시글 정보")
    PostSummary originalPost;
    @Schema(name = "reviewPost", type = "PostInfoDTO", description = "리뷰 게시글 정보")
    PostInfo reviewPost;
    @Schema(name = "isMine", type = "boolean", description = "내가 쓴 글인지 여부")
    Boolean isMine;
}

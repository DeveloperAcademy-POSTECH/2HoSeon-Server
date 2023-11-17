package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostDetail
 * @date : 11/18/23 1:05 AM
 * @modifyed : $
 **/
@Data
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PostDetail", description = "게시글 상세 정보 DTO")
public class PostDetail {
    @Schema(name = "post", type = "PostInfoDTO", description = "게시글 정보")
    PostInfo post;
    @Schema(name = "commentCount", type = "int", description = "댓글 수")
    Integer commentCount;
    @Schema(name = "commentPreview", type = "String", description = "댓글 미리보기")
    String commentPreview;

}

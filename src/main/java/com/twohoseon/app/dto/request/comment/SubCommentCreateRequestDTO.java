package com.twohoseon.app.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SubCommentCreateRequestDTO
 * @date : 11/7/23 12:00 AM
 * @modifyed : $
 **/
@Getter
@Schema(name = "SubCommentCreateRequestDTO", description = "대댓글 등록 요청 DTO")
public class SubCommentCreateRequestDTO {
    @Schema(name = "contents", type = "String", description = "댓글 내용")
    private String contents;
}

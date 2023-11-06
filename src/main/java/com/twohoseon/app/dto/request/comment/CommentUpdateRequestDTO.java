package com.twohoseon.app.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentUpdateRequestDTO
 * @date : 2023/10/21
 * @modifyed : $
 **/

@Getter
@Schema(name = "PostCommentUpdateRequestDTO", description = "댓글 수정 요청 DTO")
public class CommentUpdateRequestDTO {
    @Schema(name = "content", description = "수정할 내용")
    private String content;
}

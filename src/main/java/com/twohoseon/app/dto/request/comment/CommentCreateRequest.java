package com.twohoseon.app.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentRequestDTO
 * @date : 2023/10/18
 * @modifyed : $
 **/

@Getter
@Schema(name = "CommentCreateRequestDTO", description = "댓글 등록 요청 DTO")
public class CommentCreateRequest {
    @Schema(name = "postId", type = "long", description = "게시글 ID")
    private Long postId;
    @Schema(name = "contents", type = "String", description = "댓글 내용")
    private String contents;
}

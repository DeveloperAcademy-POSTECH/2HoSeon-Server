package com.twohoseon.app.dto.request;

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
@Schema(name = "PostCommentRequestDTO", description = "댓글 등록 요청 DTO")
public class CommentCreateRequestDTO {
    @Schema(name = "postId", description = "게시글 ID")
    private Long postId;
    @Schema(name = "content", description = "댓글 내용")
    private String content;
    @Schema(name = "parentId", description = "상위 댓글 ID")
    private Long parentId;
}

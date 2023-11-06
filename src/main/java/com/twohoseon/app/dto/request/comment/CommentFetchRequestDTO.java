package com.twohoseon.app.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentFetchRequest
 * @date : 10/31/23 4:54 PM
 * @modifyed : $
 **/
@Schema(name = "CommentFetchRequest", description = "댓글 조회 요청 DTO")
@Getter
public class CommentFetchRequestDTO {
    @Schema(name = "postId", description = "게시글 번호")
    private Long postId;
}

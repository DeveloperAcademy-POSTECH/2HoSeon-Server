package com.twohoseon.app.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentDeleteRequestDTO
 * @date : 10/31/23 4:37 PM
 * @modifyed : $
 **/
@Schema(name = "CommentDeleteRequestDTO", description = "댓글 삭제 요청 DTO")
public class CommentDeleteRequestDTO {
    private Long CommentId;

}

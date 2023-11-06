package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentInfoDTO
 * @date : 2023/10/20
 * @modifyed : $
 **/

@Getter
@Builder
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PostCommentInfoDTO", description = "댓글 정보 DTO")
public class CommentInfoDTO {
    @Schema(name = "commentId", type = "long", description = "댓글 ID")
    Long commentId;

    @Schema(name = "createDate", type = "LocalDateTime", description = "댓글 생성일")
    LocalDateTime createDate;

    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "댓글 수정일")
    LocalDateTime modifiedDate;

    @Schema(name = "contents", type = "String", description = "댓글 내용")
    String content;

    @Schema(name = "author", type = "AuthorInfoDTO", description = "댓글 작성자 정보")
    AuthorInfoDTO author;

    @Schema(name = "subComments", type = "List<PostCommentInfoDTO>", description = "대댓글 정보 DTO 리스트")
    List<CommentInfoDTO> subComments;

    public void setSubComments(List<CommentInfoDTO> subComments) {
        this.subComments = subComments;
    }

    public CommentInfoDTO(Long commentId, LocalDateTime createDate, LocalDateTime modifiedDate, String content, AuthorInfoDTO author) {
        this.commentId = commentId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.content = content;
        this.author = author;
    }
}

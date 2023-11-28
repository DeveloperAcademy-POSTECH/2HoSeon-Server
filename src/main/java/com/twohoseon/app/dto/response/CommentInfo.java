package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.dto.response.post.AuthorInfo;
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
public class CommentInfo {
    @Schema(name = "commentId", type = "long", description = "댓글 ID")
    Long commentId;

    @Schema(name = "createDate", type = "LocalDateTime", description = "댓글 생성일")
    LocalDateTime createDate;

    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "댓글 수정일")
    LocalDateTime modifiedDate;

    @Schema(name = "contents", type = "String", description = "댓글 내용")
    String content;

    @Schema(name = "author", type = "AuthorInfoDTO", description = "댓글 작성자 정보")
    AuthorInfo author;

    @Schema(name = "subComments", type = "List<PostCommentInfoDTO>", description = "대댓글 정보 DTO 리스트")
    List<CommentInfo> subComments;

    @Schema(name = "isMine", type = "Boolean", description = "내가 쓴 글인지 여부")
    Boolean isMine;

    public void setSubComments(List<CommentInfo> subComments) {
        this.subComments = subComments;
    }

    public CommentInfo(Long commentId, LocalDateTime createDate, LocalDateTime modifiedDate, String content, AuthorInfo author, boolean isMine) {
        this.commentId = commentId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.content = content;
        if (author.getId() != null)
            this.author = author;
        this.isMine = isMine;
    }
}

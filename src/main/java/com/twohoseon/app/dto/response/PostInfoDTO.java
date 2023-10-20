package com.twohoseon.app.dto.response;

import com.twohoseon.app.entity.post.enums.PostStatus;
import com.twohoseon.app.entity.post.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(name = "PostInfoDTO", description = "게시글 정보 DTO")
public class PostInfoDTO {
    @Schema(name = "post_id", type = "long", description = "게시글 ID")
    Long post_id;
    @Schema(name = "createDate", type = "LocalDateTime", description = "게시글 생성일")
    LocalDateTime createDate;
    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "게시글 수정일")
    LocalDateTime modifiedDate;
    @Schema(name = "postType", type = "PostType", description = "게시글 타입")
    PostType postType;
    @Schema(name = "postStatus", type = "PostStatus", description = "게시글 상태")
    PostStatus postStatus;
    @Schema(name = "author", type = "AuthorInfoDTO", description = "게시글 작성자 정보")
    AuthorInfoDTO author;
    @Schema(name = "title", type = "String", description = "게시글 제목")
    String title;
    @Schema(name = "contents", type = "String", description = "게시글 내용")
    String contents;
    @Schema(name = "image", type = "String", description = "게시글 이미지")
    String image;
    @Schema(name = "externalURL", type = "String", description = "게시글 외부 URL")
    String externalURL;
    @Schema(name = "likeCount", type = "int", description = "게시글 좋아요 수")
    Integer likeCount;
    @Schema(name = "viewCount", type = "int", description = "게시글 조회 수")
    Integer viewCount;
    @Schema(name = "commentCount", type = "int", description = "게시글 댓글 수")
    Integer commentCount;
    @Schema(name = "voteCounts", type = "VoteInfoDTO", description = "투표 정보")
    VoteInfoDTO voteCounts;

}
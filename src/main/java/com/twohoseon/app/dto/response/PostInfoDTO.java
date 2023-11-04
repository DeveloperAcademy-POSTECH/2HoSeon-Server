package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PostInfoDTO", description = "게시글 정보 DTO")
public class PostInfoDTO {
    @Schema(name = "postId", type = "long", description = "게시글 ID")
    Long postId;
    @Schema(name = "createDate", type = "LocalDateTime", description = "게시글 생성일")
    LocalDateTime createDate;
    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "게시글 수정일")
    LocalDateTime modifiedDate;
    //TODO Fix it
    @Schema(name = "postType", type = "PostType", description = "게시글 학교 범위")
    VisibilityScope visibilityScope;
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
    int likeCount;
    //TODO Fix it
    @Schema(name = "viewCount", type = "int", description = "게시글 조회 수")
    int viewCount;
    @Schema(name = "commentCount", type = "int", description = "게시글 댓글 수")
    int commentCount;

    @Schema(name = "isVoted", type = "boolean", description = "투표 여부")
//    @JsonProperty("isVoted")
    boolean isVoted;
    @Schema(name = "voteCounts", type = "VoteInfoDTO", description = "투표 정보")
    VoteCountsDTO voteCounts;
    @Schema(name = "voteInfoList", type = "List<VoteInfoDTO>", description = "투표 정보")
    List<VoteInfoDTO> voteInfoList;


    @Schema(name = "isMine", type = "boolean", description = "내가 쓴 글인지 여부")
    boolean isMine;

    public PostInfoDTO(Long postId, LocalDateTime createDate, LocalDateTime modifiedDate, VisibilityScope visibilityScope, AuthorInfoDTO author, String title, String contents, String image, String externalURL, int likeCount, int viewCount, int commentCount) {
        this.postId = postId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.visibilityScope = visibilityScope;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.externalURL = externalURL;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public PostInfoDTO(Long postId, LocalDateTime createDate, LocalDateTime modifiedDate, VisibilityScope visibilityScope, boolean isActive, AuthorInfoDTO author, String title, String contents, String image, String externalURL, int likeCount, int viewCount, int commentCount) {
        this.postId = postId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.visibilityScope = visibilityScope;
        this.postStatus = isActive ? PostStatus.ACTIVE : PostStatus.COMPLETE;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.externalURL = externalURL;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public void setVoteCounts(VoteCountsDTO voteCounts) {
        this.voteCounts = voteCounts;
    }

    public void setVoteInfoList(List<VoteInfoDTO> voteInfoList) {
        this.voteInfoList = voteInfoList;
    }

    public void setIsVoted(boolean isVoted) {
        this.isVoted = isVoted;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

}
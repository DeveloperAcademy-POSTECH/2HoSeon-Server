package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.dto.response.post.AuthorInfoDTO;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
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
    @Schema(name = "visibilityScope", type = "VisibilityScope", description = "게시글 공개 범위")
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
    @Schema(name = "voteCount", type = "Integer", description = "게시글 총 투표 수")
    Integer voteCount;
    @Schema(name = "commentCount", type = "Integer", description = "게시글 댓글 수")
    Integer commentCount;
    @Schema(name = "price", type = "Integer", description = "가격")
    Integer price;
    @Schema(name = "isVoted", type = "Boolean", description = "투표 여부")
    Boolean isVoted;
    @Schema(name = "voteCounts", type = "VoteInfoDTO", description = "투표 정보")
    VoteCountsDTO voteCounts;
    @Schema(name = "voteInfoList", type = "List<VoteInfoDTO>", description = "투표 정보")
    List<VoteInfoDTO> voteInfoList;

    @Schema(name = "isMine", type = "boolean", description = "내가 쓴 글인지 여부")
    Boolean isMine;

    @Schema(name = "isNotified", type = "boolean", description = "알림을 받을 것인지 여부")
    Boolean isNotified;

    @Schema(name = "isPurchased", type = "boolean", description = "구매 여부")
    Boolean isPurchased;

    public PostInfoDTO(Long postId, LocalDateTime createDate, LocalDateTime modifiedDate, VisibilityScope visibilityScope, PostStatus postStatus, AuthorInfoDTO author, String title, String contents, String image, String externalURL, int commentCount, int voteCount, int price) {
        this.postId = postId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.visibilityScope = visibilityScope;
        this.postStatus = postStatus;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.externalURL = externalURL;
        this.commentCount = commentCount;
        this.voteCount = voteCount;
        this.price = price;
    }

    //후기 상세 본문
    public PostInfoDTO(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, AuthorInfoDTO author, PostStatus postStatus, String title, String image, String contents, int price, Boolean isPurchased) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.author = author;
        this.postStatus = postStatus;
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.price = price;
        this.isPurchased = isPurchased;
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

    public void setIsNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }

}
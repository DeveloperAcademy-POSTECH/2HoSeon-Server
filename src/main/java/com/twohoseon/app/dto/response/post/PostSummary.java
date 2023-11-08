package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.dto.response.AuthorInfoDTO;
import com.twohoseon.app.entity.post.VoteResult;
import com.twohoseon.app.enums.post.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchPostInfo
 * @date : 11/7/23 1:06 AM
 * @modifyed : $
 **/
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PostSummary", description = "검색 결과 게시글 정보")
public class PostSummary {
    @Schema(name = "createDate", type = "LocalDateTime", description = "게시글 생성 시간")
    private LocalDateTime createDate;
    @Schema(name = "modifiedDate", type = "LocalDateTime", description = "게시글 수정 시간")
    private LocalDateTime modifiedDate;
    @Schema(name = "postId", type = "long", description = "게시글 ID")
    private Long postId;
    @Schema(name = "authorInfo", type = "AuthorInfoDTO", description = "게시글 작성자 정보")
    private AuthorInfoDTO authorInfo;
    @Schema(name = "postStatus", type = "PostStatus", description = "게시글 상태")
    private PostStatus postStatus;
    @Schema(name = "viewCount", type = "int", description = "게시글 조회수")
    private int viewCount;
    @Schema(name = "voteCount", type = "int", description = "총 투표 수")
    private int voteCount;
    @Schema(name = "commentCount", type = "int", description = "총 댓글 개수")
    private int commentCount;
    @Schema(name = "voteResult", type = "VoteResult", description = "게시글 투표 결과")
    private VoteResult voteResult;
    @Schema(name = "title", type = "String", description = "게시글 제목")
    private String title;
    @Schema(name = "image", type = "String", description = "게시글 이미지")
    private String image;
    @Schema(name = "contents", type = "String", description = "게시글 내용")
    private String contents;
    @Schema(name = "price", type = "int", description = "상품 가격")
    private int price;
    @Schema(name = "isPurchased", type = "boolean", description = "구매 여부")
    private boolean isPurchased;


    //진행중인 투표 검색
    public PostSummary(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, AuthorInfoDTO authorInfo, PostStatus postStatus, int voteCount, String title, String image, String contents, int price, int commentCount) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.postStatus = postStatus;
        this.voteCount = voteCount;
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.price = price;
        this.commentCount = commentCount;
    }

    //종료된 투표 검색
    public PostSummary(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, AuthorInfoDTO authorInfo, PostStatus postStatus, int voteCount, int commentCount, VoteResult voteResult, String title, String image, String contents, int price) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.postStatus = postStatus;
        this.voteCount = voteCount;
        this.commentCount = commentCount;
        this.voteResult = voteResult;
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.price = price;
    }

    //후기 검색 결과
    public PostSummary(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, AuthorInfoDTO authorInfo, PostStatus postStatus, int viewCount, int commentCount, String title, String image, String contents, int price, boolean isPurchased) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.postStatus = postStatus;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.price = price;
        this.isPurchased = isPurchased;
    }

    //소비 후기 탭 fetch
    public PostSummary(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, AuthorInfoDTO authorInfo, PostStatus postStatus, int commentCount, String title, String image, String contents, int price, boolean isPurchased) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.postStatus = postStatus;
        this.commentCount = commentCount;
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.price = price;
        this.isPurchased = isPurchased;
    }

    //소비 후기 탭 최근 후기 fetch
    public PostSummary(LocalDateTime createDate, LocalDateTime modifiedDate, Long postId, PostStatus postStatus, String title, String contents) {
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.postId = postId;
        this.postStatus = postStatus;
        this.title = title;
        this.contents = contents;
    }
}

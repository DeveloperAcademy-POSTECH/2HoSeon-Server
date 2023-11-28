package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.common.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewDetail
 * @date : 11/9/23 4:58 PM
 * @modifyed : $
 **/
@Getter
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ReviewDetail", description = "리뷰 fetch 정보")
public class ReviewDetail extends ImageDTO {
    @Schema(name = "originalPost", type = "PostSummary", description = "원본 게시글 정보")
    PostSummary originalPost;
    @Schema(name = "reviewPost", type = "PostInfoDTO", description = "리뷰 게시글 정보")
    PostInfo reviewPost;
    @Schema(name = "isMine", type = "boolean", description = "내가 쓴 글인지 여부")
    Boolean isMine;
    @Schema(name = "commentCount", type = "int", description = "댓글 수")
    Integer commentCount;
    @Schema(name = "commentPreview", type = "String", description = "댓글 미리보기")
    String commentPreview;
    @Schema(name = "commentPreviewImage", type = "String", description = "댓글 미리보기 이미지")
    String commentPreviewImage;

    public ReviewDetail(PostSummary originalPost, PostInfo reviewPost, Boolean isMine, Integer commentCount, String commentPreview, String commentPreviewImage) {
        this.originalPost = originalPost;
        this.reviewPost = reviewPost;
        this.isMine = isMine;
        this.commentCount = commentCount;
        this.commentPreview = commentPreview;
        this.commentPreviewImage = generateProfileImageURL(commentPreviewImage);
    }
}

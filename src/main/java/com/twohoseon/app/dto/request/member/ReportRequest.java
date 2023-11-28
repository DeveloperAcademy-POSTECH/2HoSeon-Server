package com.twohoseon.app.dto.request.member;

import com.twohoseon.app.enums.ReportCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportRequest
 * @date : 11/27/23 9:03 PM
 * @modifyed : $
 **/
@Getter
@Schema(description = "신고 요청 게시글 신고시 postId, 댓글 신고시 commentId")
public class ReportRequest {
    @Schema(description = "게시글 신고")
    Long postId;
    @Schema(description = "댓글 신고")
    Long commentId;
    @Schema(description = "신고 카테고리", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "신고 카테고리는 필수값입니다.")
    ReportCategory reason;
}

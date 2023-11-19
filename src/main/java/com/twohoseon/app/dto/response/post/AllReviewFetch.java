package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewFetchInfo
 * @date : 11/8/23 5:57 AM
 * @modifyed : $
 **/
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AllReviewFetch", description = "리뷰 fetch 정보")
public class AllReviewFetch {
    //나의 소비 성향 최근 리뷰
    @Schema(name = "myConsumerType", type = "ConsumerType", description = "나의 소비자 타입")
    ConsumerType myConsumerType;
    @Schema(name = "recentReviews", type = "List<PostSummary>", description = "나의 소비 성향 중 최근 리뷰")
    List<PostSummary> recentReviews;
    @Schema(name = "allReviews", type = "List<PostSummary>", description = "전체 리뷰 리스트")
    List<PostSummary> allReviews;
    @Schema(name = "purchasedReviews", type = "List<PostSummary>", description = "구매 리뷰 리스트")
    List<PostSummary> purchasedReviews;
    @Schema(name = "notPurchasedReviews", type = "List<PostSummary>", description = "미구매 리뷰 리스트")
    List<PostSummary> notPurchasedReviews;
}

package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.post.*;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewRestController
 * @date : 11/8/23 3:20 AM
 * @modifyed : $
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 조회 관련 API")
@RequestMapping("/api/reviews")
public class ReviewRestController {
    private final PostService postService;

    @GetMapping
    @Operation(summary = "리뷰 탭 조회")
    public ResponseEntity<AllReviewFetchResponse> fetchAllReviews(@RequestParam(defaultValue = "GLOBAL", value = "visibilityScope") VisibilityScope visibilityScope) {
        AllReviewFetch allReviewFetch = postService.fetchAllReviews(visibilityScope);
        AllReviewFetchResponse response = AllReviewFetchResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(allReviewFetch)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewType}")
    public ResponseEntity<ReviewFetchResponse> fetchReviews(@RequestParam(defaultValue = "GLOBAL", value = "visibilityScope") VisibilityScope visibilityScope,
                                                            @PathVariable(value = "reviewType") ReviewType reviewType,
                                                            @RequestParam(defaultValue = "0", value = "page") int page,
                                                            @RequestParam(defaultValue = "10", value = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostSummary> reviews = postService.fetchReviews(visibilityScope, pageable, reviewType);
        ReviewFetchResponse response = ReviewFetchResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(reviews)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 상세 조회")
    @GetMapping("/{reviewId}/detail")
    public ResponseEntity<ReviewDetailResponse> fetchReview(@PathVariable("reviewId") Long reviewId) {
        ReviewDetail reviewDetail = postService.fetchReviewDetailByReviewId(reviewId);
        ReviewDetailResponse response = ReviewDetailResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(reviewDetail)
                .build();
        return ok(response);
    }


}

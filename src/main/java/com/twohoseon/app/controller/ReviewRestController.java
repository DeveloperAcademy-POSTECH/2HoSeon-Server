package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.PostListResponseDTO;
import com.twohoseon.app.dto.response.post.ReviewFetch;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "리뷰 탭 게시글 조회")
    public ResponseEntity<ReviewFetch> fetchReviews(@RequestParam(defaultValue = "GLOBAL", value = "visibilityScope") VisibilityScope visibilityScope,
                                                    @RequestParam(defaultValue = "ALL", value = "reviewType") ReviewType reviewType,
                                                    @RequestParam(defaultValue = "0", value = "page") int page,
                                                    @RequestParam(defaultValue = "10", value = "size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ReviewFetch reviewFetch = postService.fetchReviews(visibilityScope, pageable, reviewType);
        PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(null)
                .build();
//        return ok(responseDTO);
        return ResponseEntity.ok(reviewFetch);
    }

}

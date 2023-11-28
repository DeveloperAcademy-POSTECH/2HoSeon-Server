package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.post.PostRequest;
import com.twohoseon.app.dto.request.post.VoteCreateRequest;
import com.twohoseon.app.dto.request.review.ReviewRequest;
import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.dto.response.VoteCounts;
import com.twohoseon.app.dto.response.VoteResultResponse;
import com.twohoseon.app.dto.response.post.*;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostRestController
 * @date : 10/18/23 4:04 PM
 * @modifyed : $
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponse> createPost(@RequestPart(value = "postRequest") PostRequest postRequest,
                                                      @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.createPost(postRequest, file);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "투표 즉시 완료", description = "투표 즉시 완료")
    @PostMapping(value = "/{postId}/complete")
    public ResponseEntity<GeneralResponse> completeVote(@PathVariable("postId") Long postId) {
        postService.completeVote(postId);

        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponse> updatePost(@PathVariable("postId") Long postId,
                                                      @RequestPart(value = "postRequest") PostRequest postUpdateRequest,
                                                      @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.updatePost(postId, postUpdateRequest, file);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<GeneralResponse> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "후기 구독", description = "후기 구독")
    @PostMapping("/{postId}/subscribe")
    public ResponseEntity<GeneralResponse> subscribePost(@PathVariable("postId") Long postId) {
        //TODO 중복 구독 핸들링
        postService.subscribePost(postId);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "후기 구독 취소", description = "후기 구독")
    @DeleteMapping("/{postId}/subscribe")
    public ResponseEntity<GeneralResponse> unSubscribePost(@PathVariable("postId") Long postId) {
        //TODO 중복 구독 핸들링
        postService.unsubscribePost(postId);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "리뷰 작성", description = "리뷰 작성")
    @PostMapping(value = "/{postId}/reviews", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponse> createReview(@PathVariable("postId") Long postId,
                                                        @RequestPart(value = "reviewRequest") ReviewRequest reviewRequest,
                                                        @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.createReview(postId, reviewRequest, file);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 상세 조회")
    @GetMapping("/{postId}/reviews")
    public ResponseEntity<ReviewDetailResponse> fetchReview(@PathVariable("postId") Long postId) {
        ReviewDetail reviewDetail = postService.fetchReviewDetail(postId);
        ReviewDetailResponse response = ReviewDetailResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(reviewDetail)
                .build();
        return ok(response);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 수정")
    @PutMapping(value = "/{postId}/reviews", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponse> updateReview(@PathVariable("postId") Long postId,
                                                        @RequestPart(value = "reviewRequest") ReviewRequest reviewRequest,
                                                        @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.updateReview(postId, reviewRequest, file);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제")
    @DeleteMapping("/{postId}/reviews")
    public ResponseEntity<GeneralResponse> deleteReview(@PathVariable("postId") Long postId) {
        postService.deleteReview(postId);
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(response);
    }


    @GetMapping
    @Operation(summary = "게시글 조회", description = "게시글 조회")
    public ResponseEntity<PostListResponse> fetchPosts(@RequestParam(defaultValue = "0", value = "page") int page,
                                                       @RequestParam(defaultValue = "10", value = "size") int size,
                                                       @RequestParam(value = "visibilityScope") VisibilityScope visibilityScope) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostInfo> postInfoList = postService.fetchPosts(pageable, visibilityScope);
        PostListResponse response = PostListResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postInfoList)
                .build();
        return ok(response);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회")
    public ResponseEntity<PostDetailResponse> fetchPostDetail(@PathVariable("postId") Long postId) {
        PostDetail postInfo = postService.fetchPostDetail(postId);
        PostDetailResponse response = PostDetailResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postInfo)
                .build();
        return ok(response);
    }


    @Operation(summary = "게시글 투표 하기", description = "게시글 투표하기")
    @PostMapping("/{postId}/votes")
    public ResponseEntity<VoteResultResponse> vote(@PathVariable("postId") Long postId, @RequestBody VoteCreateRequest voteCreateRequest) {
        VoteCounts voteCounts = postService.createVote(postId, voteCreateRequest.isMyChoice());
        VoteResultResponse response = VoteResultResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(voteCounts)
                .build();
        return ok(response);
    }

}

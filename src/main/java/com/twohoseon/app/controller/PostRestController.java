package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.post.VoteCreateRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.GeneralResponseDTO;
import com.twohoseon.app.dto.response.PostListResponseDTO;
import com.twohoseon.app.dto.response.PostResponseDTO;
import com.twohoseon.app.dto.response.VoteResultResponseDTO;
import com.twohoseon.app.dto.response.post.ReviewDetailResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.comment.CommentService;
import com.twohoseon.app.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final CommentService commentService;
    private final PostRepository postRepository;

    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> createPost(@RequestPart(value = "postRequest") PostRequestDTO postRequestDTO,
                                                         @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.createPost(postRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> updatePost(@PathVariable("postId") Long postId,
                                                         @RequestPart(value = "postRequest") PostRequestDTO postUpdateRequestDTO,
                                                         @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.updatePost(postId, postUpdateRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<GeneralResponseDTO> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "후기 구독", description = "후기 구독")
    @PostMapping("/{postId}/subscribe")
    public ResponseEntity<GeneralResponseDTO> subscribePost(@PathVariable("postId") Long postId) {
        //TODO 중복 구독 핸들링
        postService.subscribePost(postId);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 작성", description = "리뷰 작성")
    @PostMapping(value = "/{postId}/reviews", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> createReview(@PathVariable("postId") Long postId,
                                                           @RequestPart(value = "reviewRequest") ReviewRequestDTO reviewRequestDTO,
                                                           @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.createReview(postId, reviewRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 상세 조회")
    @GetMapping("/{postId}/reviews")
    public ResponseEntity<ReviewDetailResponse> fetchReview(@PathVariable("postId") Long postId) {
        ReviewDetailResponse responseDTO = ReviewDetailResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.getReviewDetail(postId))
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 수정")
    @PutMapping(value = "/{postId}/reviews", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> updateReview(@PathVariable("postId") Long postId,
                                                           @RequestPart(value = "reviewRequest") ReviewRequestDTO reviewRequestDTO,
                                                           @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        postService.updateReview(postId, reviewRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제")
    @DeleteMapping("/{postId}/reviews")
    public ResponseEntity<GeneralResponseDTO> deleteReview(@PathVariable("postId") Long postId) {
        postService.deleteReview(postId);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }


    @GetMapping
    @Operation(summary = "게시글 조회", description = "게시글 조회")
    public ResponseEntity<PostListResponseDTO> fetchPosts(@RequestParam(defaultValue = "0", value = "page") int page,
                                                          @RequestParam(defaultValue = "10", value = "size") int size,
                                                          @RequestParam(value = "visibilityScope") VisibilityScope visibilityScope) {
        Pageable pageable = PageRequest.of(page, size);

        PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.fetchPosts(pageable, visibilityScope))
                .build();
        return ok(responseDTO);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회")
    public ResponseEntity<PostResponseDTO> fetchPost(@PathVariable("postId") Long postId) {
        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.fetchPost(postId))
                .build();
        return ok(responseDTO);
    }


    @Operation(summary = "게시글 투표 하기", description = "게시글 투표하기")
    @ApiResponse(
            responseCode = "200",
            description = "투표 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VoteResultResponseDTO.class)
            )
    )
    @PostMapping("/{postId}/votes")
    public ResponseEntity<VoteResultResponseDTO> vote(@PathVariable("postId") Long postId, @RequestBody VoteCreateRequestDTO voteCreateRequestDTO) {

        VoteResultResponseDTO responseDTO = VoteResultResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.createVote(postId, voteCreateRequestDTO.getVoteType()))
                .build();
        return ok(responseDTO);
    }

}

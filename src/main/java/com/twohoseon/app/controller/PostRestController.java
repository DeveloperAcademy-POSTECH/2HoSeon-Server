package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.comment.CommentCreateRequestDTO;
import com.twohoseon.app.dto.request.comment.CommentFetchRequestDTO;
import com.twohoseon.app.dto.request.comment.CommentUpdateRequestDTO;
import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.post.VoteCreateRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.*;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.post.PostCommentService;
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
    private final PostCommentService postCommentService;
    private final PostRepository postRepository;

    @Operation(summary = "게시글 작성")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> createPost(@RequestPart("postRequest") PostRequestDTO postRequestDTO,
                                                         @RequestPart("imageFile") MultipartFile file) {
        postService.createPost(postRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> updatePost(@PathVariable Long postId,
                                                         @RequestPart("postRequest") PostRequestDTO postUpdateRequestDTO,
                                                         @RequestPart("imageFile") MultipartFile file) {
        postService.updatePost(postId, postUpdateRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<GeneralResponseDTO> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "후기 구독")
    @PostMapping("/{postId}/subscribe")
    public ResponseEntity<GeneralResponseDTO> subscribePost(@PathVariable Long postId) {
        postService.subscribePost(postId);
        return ok().build();
    }

    @Operation(summary = "리뷰 작성")
    @PostMapping(value = "/{postId}/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> createReview(@PathVariable Long postId,
                                                           @RequestPart("reviewRequest") ReviewRequestDTO reviewRequestDTO,
                                                           @RequestPart("imageFile") MultipartFile file) {
        postService.createReview(postId, reviewRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping(value = "/{postId}/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponseDTO> updateReview(@PathVariable Long postId,
                                                           @RequestPart("reviewRequest") ReviewRequestDTO reviewRequestDTO,
                                                           @RequestPart("imageFile") MultipartFile file) {
        postService.updateReview(postId, reviewRequestDTO, file);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{postId}/reviews")
    public ResponseEntity<GeneralResponseDTO> deleteReview(@PathVariable Long postId) {
        postService.deleteReview(postId);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }
//
//    @Operation(summary = "리뷰 조회")
//    @GetMapping("/{postId}/reviews")
//    public ResponseEntity<GeneralResponseDTO> readReview(@PathVariable Long postId) {
//        //TODO 리뷰 조회 구현하기.
//        return ok().build();
//    }
//
//    @Operation(summary = "리뷰 댓글 작성")
//    @PostMapping("/{postId}/reviews/comments")
//    public ResponseEntity<GeneralResponseDTO> createReviewComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
//        //TODO 리뷰 댓글 작성 구현하기.
//        return ok().build();
//    }
//
//    @Operation(summary = "리뷰 댓글 수정")
//    @PutMapping("/{postId}/reviews/comments/{commentId}")
//    public ResponseEntity<GeneralResponseDTO> updateReviewComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO) {
//        //TODO 리뷰 댓글 수정 구현하기.
//        return ok().build();
//    }
//
//    @Operation(summary = "리뷰 댓글 삭제")
//    @DeleteMapping("/{postId}/reviews/comments/{commentId}")
//    public ResponseEntity<GeneralResponseDTO> deleteReviewComment(@PathVariable Long postId, @PathVariable Long commentId) {
//        //TODO 리뷰 댓글 삭제 구현하기.
//        return ok().build();
//    }

    @GetMapping
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostListResponseDTO> fetchPosts(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "ACTIVE") PostStatus postStatus) {
        Pageable pageable = PageRequest.of(page, size);

        PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.fetchPosts(pageable, postStatus))
                .build();
        return ok(responseDTO);
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> fetchPost(@PathVariable Long postId) {
        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.fetchPost(postId))
                .build();
        return ok(responseDTO);
    }


    @Operation(summary = "게시글 투표 하기")
    @PostMapping("/{postId}/votes")
    public ResponseEntity<VoteResultResponseDTO> vote(@PathVariable Long postId, @RequestBody VoteCreateRequestDTO voteCreateRequestDTO) {

        VoteResultResponseDTO responseDTO = VoteResultResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.createVote(postId, voteCreateRequestDTO.getVoteType()))
                .build();
        return ok(responseDTO);
    }


    //TODO 좋아요
    //TODO 좋아요 취소

    @Operation(summary = "댓글 작성")
    @PostMapping("/comments")
    public ResponseEntity<GeneralResponseDTO> createPostComment(@RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {

        postCommentService.createComment(commentCreateRequestDTO);
        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("create success")
                .build();
        return ok(generalResponseDTO);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<GeneralResponseDTO> deletePostComment(@PathVariable(value = "commentId") Long postCommentId) {


        postCommentService.deleteComment(postCommentId);

        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("delete success")
                .build();

        return ok(generalResponseDTO);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<GeneralResponseDTO> updatePostComment(@PathVariable(value = "commentId") Long postCommentId,
                                                                @RequestBody CommentUpdateRequestDTO postCommentUpdateRequestDTO) {

        postCommentService.updateComment(postCommentId, postCommentUpdateRequestDTO.getContent());

        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("update success")
                .build();

        return ok(generalResponseDTO);
    }

    @Operation(summary = "댓글 조회")
    @GetMapping("/comments")
    public ResponseEntity<CommentResponseDTO> readPostComment(@RequestBody CommentFetchRequestDTO commentFetchRequestDTO) {

        List<CommentInfoDTO> postCommentLists = postRepository.getAllCommentsFromPost(commentFetchRequestDTO.getPostId());

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postCommentLists)
                .build();

        return ok(commentResponseDTO);
    }

    @Operation(summary = "대댓글 조회")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> getPostCommentChildren(@PathVariable Long commentId) {

        List<CommentInfoDTO> postCommentLists = postRepository.getChildComments(commentId);

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postCommentLists)
                .build();

        return ok(commentResponseDTO);
    }
}

package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.PostCommentRequestDTO;
import com.twohoseon.app.dto.request.PostCommentUpdateRequestDTO;
import com.twohoseon.app.dto.request.PostCreateRequestDTO;
import com.twohoseon.app.dto.request.VoteCreateRequestDTO;
import com.twohoseon.app.dto.response.*;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.post.PostCommentService;
import com.twohoseon.app.service.post.PostLikeService;
import com.twohoseon.app.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final PostLikeService postLikeService;
    private final PostCommentService postCommentService;
    private final PostRepository postRepository;

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ResponseEntity<GeneralResponseDTO> createPost(@RequestBody PostCreateRequestDTO postCreateRequestDTO) {
        postService.createPost(postCreateRequestDTO);
        GeneralResponseDTO responseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", useReturnTypeSchema = true),
    })
    public ResponseEntity<PostListResponseDTO> fetchPosts(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postService.fetchPosts(pageable))
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
    @PostMapping("/{postId}/comments")
    public ResponseEntity<GeneralResponseDTO> createPostComment(@PathVariable Long postId, @RequestBody PostCommentRequestDTO postCommentRequestDTO) {

        postCommentService.createComment(postId, postCommentRequestDTO);

        GeneralResponseDTO.GeneralResponseDTOBuilder responseDTOBuilder = GeneralResponseDTO.builder();

        responseDTOBuilder
                .status(StatusEnum.OK)
                .message("create success");

        return ok(responseDTOBuilder.build());
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<GeneralResponseDTO> deletePostComment(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long postCommentId) {

        postCommentService.removeComment(postId, postCommentId);

        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("delete success")
                .build();

        return ok(generalResponseDTO);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<GeneralResponseDTO> updatePostComment(@PathVariable(value = "postId") Long postId,
                                                                @PathVariable(value = "commentId") Long postCommentId,
                                                                @RequestBody PostCommentUpdateRequestDTO postCommentUpdateRequestDTO) {

        postCommentService.updateComment(postId, postCommentId, postCommentUpdateRequestDTO.getContent());

        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("update success")
                .build();

        return ok(generalResponseDTO);
    }

    @Operation(summary = "댓글 조회")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PostCommentResponseDTO> readPostComment(@PathVariable Long postId) {

        List<PostCommentInfoDTO> postCommentLists = postRepository.getAllCommentsFromPost(postId);

        PostCommentResponseDTO postCommentResponseDTO = PostCommentResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postCommentLists)
                .build();

        return ok(postCommentResponseDTO);
    }

    @Operation(summary = "좋아요 등록")
    @PostMapping("/{postId}/likes")
    public ResponseEntity<GeneralResponseDTO> likePost(@PathVariable Long postId) {

        postLikeService.likePost(postId);

        GeneralResponseDTO.GeneralResponseDTOBuilder responseDTOBuilder = GeneralResponseDTO.builder();

        responseDTOBuilder
                .status(StatusEnum.OK)
                .message("check success");

        return ok(responseDTOBuilder.build());
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<GeneralResponseDTO> unlikePost(@PathVariable Long postId) {

        postLikeService.unlikePost(postId);

        GeneralResponseDTO.GeneralResponseDTOBuilder responseDTOBuilder = GeneralResponseDTO.builder();

        responseDTOBuilder
                .status(StatusEnum.OK)
                .message("delete success");

        return ok(responseDTOBuilder.build());
    }
}

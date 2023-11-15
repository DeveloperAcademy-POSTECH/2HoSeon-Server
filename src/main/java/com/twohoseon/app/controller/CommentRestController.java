package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.comment.CommentCreateRequest;
import com.twohoseon.app.dto.request.comment.CommentUpdateRequest;
import com.twohoseon.app.dto.request.comment.SubCommentCreateRequest;
import com.twohoseon.app.dto.response.CommentInfo;
import com.twohoseon.app.dto.response.CommentResponse;
import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentRestController
 * @date : 11/6/23 10:29 PM
 * @modifyed : $
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
@RequestMapping("/api/comments")
public class CommentRestController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;


    @Operation(summary = "댓글 작성")
    @PostMapping
    public ResponseEntity<GeneralResponse> createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        commentService.createComment(commentCreateRequest);
        GeneralResponse generalResponse = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("create success")
                .build();
        return ok(generalResponse);
    }

    @Operation(summary = "포스트 댓글 조회")
    @GetMapping
    public ResponseEntity<CommentResponse> readPostComment(@RequestParam("postId") Long postId) {
        List<CommentInfo> commentList = commentService.getPostComments(postId);

        CommentResponse commentResponse = CommentResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(commentList)
                .build();

        return ok(commentResponse);
    }

    @Operation(summary = "대댓글 작성")
    @PostMapping("/{commentId}")
    public ResponseEntity<GeneralResponse> createSubComment(@PathVariable(value = "commentId") Long commentId,
                                                            @RequestBody SubCommentCreateRequest subCommentCreateRequest) {
        commentService.createSubComment(commentId, subCommentCreateRequest);
        GeneralResponse response = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("create success")
                .build();
        return ok(response);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<GeneralResponse> deletePostComment(@PathVariable(value = "commentId") Long postCommentId) {


        commentService.deleteComment(postCommentId);

        GeneralResponse response = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("delete success")
                .build();

        return ok(response);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<GeneralResponse> updatePostComment(@PathVariable(value = "commentId") Long postCommentId,
                                                             @RequestBody CommentUpdateRequest postCommentUpdateRequest) {

        commentService.updateComment(postCommentId, postCommentUpdateRequest.getContent());

        GeneralResponse response = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("update success")
                .build();

        return ok(response);
    }


    @Operation(summary = "대댓글 조회")
    @GetMapping("/{commentId}/sub-comments")
    public ResponseEntity<CommentResponse> getPostCommentChildren(@PathVariable(value = "commentId") Long commentId) {

        List<CommentInfo> postCommentLists = commentService.getSubComments(commentId);
        commentRepository.getSubComments(commentId);

        CommentResponse commentResponse = CommentResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postCommentLists)
                .build();

        return ok(commentResponse);
    }
}

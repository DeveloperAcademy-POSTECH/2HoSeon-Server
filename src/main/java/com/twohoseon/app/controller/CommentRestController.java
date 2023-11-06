package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.comment.CommentCreateRequestDTO;
import com.twohoseon.app.dto.request.comment.CommentUpdateRequestDTO;
import com.twohoseon.app.dto.request.comment.SubCommentCreateRequestDTO;
import com.twohoseon.app.dto.response.CommentInfoDTO;
import com.twohoseon.app.dto.response.CommentResponseDTO;
import com.twohoseon.app.dto.response.GeneralResponseDTO;
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
    public ResponseEntity<GeneralResponseDTO> createComment(@RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
        commentService.createComment(commentCreateRequestDTO);
        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("create success")
                .build();
        return ok(generalResponseDTO);
    }

    @Operation(summary = "포스트 댓글 조회")
    @GetMapping
    public ResponseEntity<CommentResponseDTO> readPostComment(@RequestParam Long postId) {
        List<CommentInfoDTO> commentList = commentService.getPostComments(postId);

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(commentList)
                .build();

        return ok(commentResponseDTO);
    }

    @Operation(summary = "대댓글 작성")
    @PostMapping("/{commentId}")
    public ResponseEntity<GeneralResponseDTO> createSubComment(@PathVariable(value = "commentId") Long commentId,
                                                               @RequestBody SubCommentCreateRequestDTO subCommentCreateRequestDTO) {
        commentService.createSubComment(commentId, subCommentCreateRequestDTO);
        GeneralResponseDTO response = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("create success")
                .build();
        return ok(response);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<GeneralResponseDTO> deletePostComment(@PathVariable(value = "commentId") Long postCommentId) {


        commentService.deleteComment(postCommentId);

        GeneralResponseDTO response = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("delete success")
                .build();

        return ok(response);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<GeneralResponseDTO> updatePostComment(@PathVariable(value = "commentId") Long postCommentId,
                                                                @RequestBody CommentUpdateRequestDTO postCommentUpdateRequestDTO) {

        commentService.updateComment(postCommentId, postCommentUpdateRequestDTO.getContent());

        GeneralResponseDTO response = GeneralResponseDTO
                .builder()
                .status(StatusEnum.OK)
                .message("update success")
                .build();

        return ok(response);
    }


    @Operation(summary = "대댓글 조회")
    @GetMapping("/{commentId}/sub-comments")
    public ResponseEntity<CommentResponseDTO> getPostCommentChildren(@PathVariable Long commentId) {

        List<CommentInfoDTO> postCommentLists = commentService.getSubComments(commentId);
        commentRepository.getSubComments(commentId);

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(postCommentLists)
                .build();

        return ok(commentResponseDTO);
    }
}

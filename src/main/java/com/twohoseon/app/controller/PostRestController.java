package com.twohoseon.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class PostRestController {
    //TODO 게시글 작성
    @GetMapping("/api/posts")
    public ResponseEntity createPost(@RequestBody PostCreateRequestDTO postCreateRequestDTO) {
        return ResponseEntity.ok("s");
    }

    //TODO 게시글 수정 - 나중
    //TODO 게시글 삭제 - 나중
    //TODO 게시글 조회 paging

    //TODO 투표
    //TODO 댓글 작성

    //TODO 좋아요
    //TODO 좋아요 취소

    //TODO 조회수
}

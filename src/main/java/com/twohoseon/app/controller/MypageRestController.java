package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.mypage.MypageFetch;
import com.twohoseon.app.dto.response.mypage.MypageFetchResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MyPageRestController
 * @date : 11/13/23 4:16 AM
 * @modifyed : $
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "MyPage", description = "마이페이지 관련 API")
public class MypageRestController {
    private final PostService postService;


    @GetMapping("/posts")
    @Operation(summary = "게시글 조회", description = "게시글 조회")
    public ResponseEntity<MypageFetchResponse> fetchPosts(@RequestParam(defaultValue = "0", value = "page") int page,
                                                          @RequestParam(defaultValue = "10", value = "size") int size,
                                                          @RequestParam(value = "myVoteCategoryType") MyVoteCategoryType myVoteCategoryType) {
        Pageable pageable = PageRequest.of(page, size);
        MypageFetch mypageFetch = postService.fetchMypagePosts(pageable, myVoteCategoryType);
        MypageFetchResponse response = MypageFetchResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(mypageFetch)
                .build();
        return ok(response);
    }

    @GetMapping("/reviews")
    @Operation(summary = "마이페이지 리뷰 조회", description = "마이페이지 리뷰 조회")
    public ResponseEntity<MypageFetchResponse> fetchReviews(@RequestParam(value = "visibilityScope") VisibilityScope visibilityScope,
                                                            @RequestParam(defaultValue = "0", value = "page") int page,
                                                            @RequestParam(defaultValue = "10", value = "size") int size) {

        Pageable pageable = PageRequest.of(page, size);

        MypageFetch mypageFetch = postService.fetchMyReviews(visibilityScope, pageable);
        MypageFetchResponse response = MypageFetchResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .data(mypageFetch)
                .build();
        return ok(response);
    }
}

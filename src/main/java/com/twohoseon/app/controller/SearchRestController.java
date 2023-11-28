package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.dto.response.post.SearchResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.service.search.SearchService;
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

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchRestController
 * @date : 2023/10/22
 * @modifyed : $
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Search", description = "검색 관련 API")
@RequestMapping("/api/search")
public class SearchRestController {

    private final SearchService searchService;

    @Operation(summary = "검색")
    @GetMapping
    public ResponseEntity<SearchResponse> searchKeyword(@RequestParam(value = "postStatus") PostStatus postStatus,
                                                        @RequestParam(value = "visibilityScope") VisibilityScope visibilityScope,
                                                        @RequestParam(defaultValue = "0", value = "page") int page,
                                                        @RequestParam(defaultValue = "10", value = "size") int size,
                                                        @RequestParam(defaultValue = "", value = "keyword") String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostSummary> postInfoList = searchService.getSearchByKeyword(postStatus, visibilityScope, pageable, keyword);
        SearchResponse response = SearchResponse.builder()
                .status(StatusEnum.OK)
                .message("search success")
                .data(postInfoList)
                .build();

        return ok(response);
    }

}

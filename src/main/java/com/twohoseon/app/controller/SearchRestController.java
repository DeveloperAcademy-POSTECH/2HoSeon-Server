package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.PostResponseDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.search.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PostResponseDTO> searchKeyword(@RequestParam(defaultValue = "") String keyword) {

        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("search success")
                .data(searchService.getSearchByKeyword(keyword))
                .build();

        return ok(responseDTO);
    }

}

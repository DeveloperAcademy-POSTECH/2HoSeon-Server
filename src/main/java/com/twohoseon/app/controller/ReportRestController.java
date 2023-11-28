package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.member.ReportRequest;
import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReportRestController
 * @date : 11/27/23 8:59 PM
 * @modifyed : $
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "신고 관련 API")
public class ReportRestController {

    private final ReportService reportService;

    @PostMapping("/posts")
    public ResponseEntity<GeneralResponse> reportPost(@RequestBody ReportRequest reportRequest) {
        reportService.reportPost(reportRequest);
        GeneralResponse generalResponse = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("report success")
                .build();
        return ResponseEntity.ok(generalResponse);
    }

    @PostMapping("/comments")
    public ResponseEntity<GeneralResponse> reportComment(@RequestBody ReportRequest reportRequest) {
        reportService.reportComment(reportRequest);
        GeneralResponse generalResponse = GeneralResponse
                .builder()
                .status(StatusEnum.OK)
                .message("report success")
                .build();
        return ResponseEntity.ok(generalResponse);
    }
}

package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.member.RegisterDeviceTokenRequest;
import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.member.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NotificationRestController
 * @date : 10/31/23 9:28 PM
 * @modifyed : $
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "알림 관련 API")
public class NotificationRestController {
    private final MemberService memberService;

    @PostMapping("/tokens")
    public ResponseEntity<GeneralResponse> registerToken(@RequestBody RegisterDeviceTokenRequest request) {
        memberService.registerToken(request.getDeviceToken());
        GeneralResponse responseDTO = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("device token registered")
                .build();
        return ResponseEntity.ok(responseDTO);
    }
}

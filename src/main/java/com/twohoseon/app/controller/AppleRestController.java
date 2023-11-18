package com.twohoseon.app.controller;

import com.twohoseon.app.dto.apple.DeleteAppleUserRequest;
import com.twohoseon.app.service.apple.AppleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : AppleRestController
 * @date : 11/17/23 4:30 PM
 * @modifyed : $
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/apple/notification")
@Slf4j
public class AppleRestController {
    private final AppleService appleService;

    @PostMapping
    public void listenAppleRequest(@RequestBody DeleteAppleUserRequest deleteAppleUserRequest) {

        appleService.listenDeleteMemberEvent(deleteAppleUserRequest.getPayload());
    }
}

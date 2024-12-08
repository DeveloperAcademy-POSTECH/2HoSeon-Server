package com.twohoseon.app.controller;

import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ShowcaseController
 * @date : 12/2/23 4:51 PM
 * @modifyed : $
 **/
@RestController
@RequiredArgsConstructor
public class ShowcaseController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PutMapping("/api/showcase/detach")
    public ResponseEntity<GeneralResponse> detachVoteFromSunday() {
        memberService.detachVoteFromMember();
        GeneralResponse response = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ResponseEntity.ok(response);
    }

}

package com.twohoseon.app.controller;

import com.twohoseon.app.dto.ProfileRequestDTO;
import com.twohoseon.app.dto.ResultDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/api/profiles")
    public ResponseEntity<ResultDTO> setMemberInfo(@RequestBody ProfileRequestDTO profileRequestDTO) {
        memberService.setUserProfile(profileRequestDTO);

        ResultDTO resultDTO = ResultDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping("/api/profiles/isValidNickname")
    public ResponseEntity<ResultDTO> checkNicknameDuplicate(String userNickname) {

        Map<String, Boolean> result = new HashMap<>();
        result.put("isExist", memberService.validateDuplicateUserNickname(userNickname));
        ResultDTO resultDTO = ResultDTO.builder()
                .status(StatusEnum.OK)
                .message("생공")
                .data(result)
                .build();

        return ResponseEntity.ok(resultDTO);
    }
}

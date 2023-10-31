package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.NicknameValidCheckRequest;
import com.twohoseon.app.dto.request.ProfileRequestDTO;
import com.twohoseon.app.dto.response.GeneralResponseDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 정보 관련 API")
public class MemberRestController {

    private final MemberService memberService;


    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @PostMapping("/api/profiles")
    public ResponseEntity<GeneralResponseDTO> setMemberInfo(@RequestBody ProfileRequestDTO profileRequestDTO) {
        memberService.setUserProfile(profileRequestDTO);
        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ResponseEntity.ok(generalResponseDTO);
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
    @PostMapping("/api/profiles/isValidNickname")
    public ResponseEntity<GeneralResponseDTO> checkNicknameDuplicate(@RequestBody NicknameValidCheckRequest request) {
        String userNickname = request.getUserNickname();
        GeneralResponseDTO.GeneralResponseDTOBuilder resultBuilder = GeneralResponseDTO.builder();
        boolean isExist = memberService.validateDuplicateUserNickname(userNickname);
        log.debug("userNickname : {}", userNickname);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isExist", isExist);

        if (!isExist) {
            resultBuilder
                    .status(StatusEnum.OK)
                    .message("unique nickname")
                    .data(result);
        } else {
            resultBuilder
                    .status(StatusEnum.CONFLICT)
                    .message("duplicate nickname")
                    .data(result);
        }
        return ResponseEntity.ok(resultBuilder.build());
    }
}

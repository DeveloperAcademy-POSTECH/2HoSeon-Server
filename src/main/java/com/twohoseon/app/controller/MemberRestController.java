package com.twohoseon.app.controller;

import com.twohoseon.app.dto.ConsumerTypeRequest;
import com.twohoseon.app.dto.request.member.NicknameValidCheckRequest;
import com.twohoseon.app.dto.request.member.ProfileRequest;
import com.twohoseon.app.dto.response.GeneralResponse;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 정보 관련 API")
public class MemberRestController {

    private final MemberService memberService;


    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @PostMapping(value = "/api/profiles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralResponse> setMemberInfo(@RequestPart(value = "profileRequest") ProfileRequest profileRequest,
                                                         @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        memberService.setUserProfile(profileRequest, imageFile);

        GeneralResponse generalResponse = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();

        return ResponseEntity.ok(generalResponse);
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
    @PostMapping("/api/profiles/isValidNickname")
    public ResponseEntity<GeneralResponse> checkNicknameDuplicate(@RequestBody NicknameValidCheckRequest request) {
        String userNickname = request.getNickname();
        GeneralResponse.GeneralResponseBuilder resultBuilder = GeneralResponse.builder();
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

    @Operation(summary = "소비 성향 설정", description = "소비 성향 설정")
    @PutMapping("/api/profiles/consumerType")
    public ResponseEntity<GeneralResponse> setConsumptionType(@RequestBody ConsumerTypeRequest consumerTypeRequest) {
        memberService.setConsumptionTendency(consumerTypeRequest);
        GeneralResponse generalResponse = GeneralResponse.builder()
                .status(StatusEnum.OK)
                .message("success")
                .build();
        return ResponseEntity.ok(generalResponse);
    }


}

package com.twohoseon.app.controller;

import com.twohoseon.app.dto.request.TokenRefreshDTO;
import com.twohoseon.app.dto.response.GeneralResponseDTO;
import com.twohoseon.app.dto.response.TokenDTO;
import com.twohoseon.app.enums.StatusEnum;
import com.twohoseon.app.service.refreshToken.RefreshTokenService;
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
 * @name : TokenRestController
 * @date : 10/16/23 3:54AM
 * @modifyed : $
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthRestController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("refresh")
    public ResponseEntity<GeneralResponseDTO> tokenRefresh(@RequestBody TokenRefreshDTO tokenRefreshDTO) {
        log.debug("token refresh request.");
        log.debug("refresh token : {}", tokenRefreshDTO.getRefreshToken());
        log.debug("identifier : {}", tokenRefreshDTO.getIdentifier());
        TokenDTO renewToken = refreshTokenService.renewToken(tokenRefreshDTO.getRefreshToken(), tokenRefreshDTO.getIdentifier());
        GeneralResponseDTO generalResponseDTO = GeneralResponseDTO.builder()
                .status(StatusEnum.OK)
                .message("token renew success.")
                .data(renewToken)
                .build();
        return ResponseEntity.ok(generalResponseDTO);
    }
}

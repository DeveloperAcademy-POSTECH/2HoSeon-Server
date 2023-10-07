package com.twohoseon.app.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : TokenDTO
 * @date : 2023/10/07 6:20 PM
 * @modifyed : $
 **/
@Builder
@Getter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}

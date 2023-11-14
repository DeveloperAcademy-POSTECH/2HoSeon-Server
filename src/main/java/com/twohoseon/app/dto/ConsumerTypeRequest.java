package com.twohoseon.app.dto;

import com.twohoseon.app.enums.ConsumerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ConsumerTypeRequest
 * @date : 11/14/23 2:30 PM
 * @modifyed : $
 **/
@Getter
@Schema(name = "ConsumerTypeRequest", description = "소비자 타입")
public class ConsumerTypeRequest {
    @Schema(name = "consumerType", description = "소비자 타입")
    private ConsumerType consumerType;
}

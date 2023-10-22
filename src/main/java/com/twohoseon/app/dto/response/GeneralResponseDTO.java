package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ResultDTO
 * @date : 2023/10/07 6:17 PM
 * @modifyed : $
 **/
@Setter
@Getter
@Schema(name = "GeneralResponseDTO", description = "공통 응답 DTO")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponseDTO<T> {
    @Schema(name = "status", type = "int", description = "응답 상태")
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;

    @Schema(name = "message", description = "응답 메시지")
    private String message;

    @Schema(name = "data", description = "응답 데이터")
    private T data;
}

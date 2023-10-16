package com.twohoseon.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ResultDTO
 * @date : 2023/10/07 6:17 PM
 * @modifyed : $
 **/
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO {
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;
    private String message;
    private Object data;
}

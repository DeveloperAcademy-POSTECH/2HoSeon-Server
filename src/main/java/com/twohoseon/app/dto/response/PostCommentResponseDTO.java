package com.twohoseon.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentResponseDTO
 * @date : 2023/10/20
 * @modifyed : $
 **/

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PostCommentResponseDTO", description = "댓글 응답 DTO")
public class PostCommentResponseDTO {
    @Schema(name = "status", type = "int", description = "응답 상태")
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;

    @Schema(name = "message", description = "응답 메시지")
    private String message;

    @Schema(name = "data", description = "응답 데이터")
    private List<PostCommentInfoDTO> data;
}

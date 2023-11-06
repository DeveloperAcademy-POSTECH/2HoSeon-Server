package com.twohoseon.app.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twohoseon.app.common.StatusEnumSerializer;
import com.twohoseon.app.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostListResponseDTO
 * @date : 10/20/23 12:43 AM
 * @modifyed : $
 **/
@Data
@Schema(name = "SearchResponseDTO", description = "게시글 응답 DTO")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponseDTO {
    @Schema(name = "status", type = "int", description = "응답 상태")
    @JsonSerialize(using = StatusEnumSerializer.class)
    private StatusEnum status;

    @Schema(name = "message", type = "String", description = "응답 메시지")
    private String message;

    @Schema(name = "data", type = "List<PostInfoDTO>", description = "응답 데이터")
    private List<SearchPostInfo> data;
}

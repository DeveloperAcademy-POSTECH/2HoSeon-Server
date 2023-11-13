package com.twohoseon.app.dto.response.mypage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twohoseon.app.dto.response.post.PostSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MypageFetch
 * @date : 11/13/23 4:30 AM
 * @modifyed : $
 **/
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "MypageFetch", description = "마이페이지 fetch 정보")
public class MypageFetch {
    @Schema(name = "total", type = "Integer", description = "총 개수")
    Integer total;

    @Schema(name = "posts", type = "List<PostSummary>", description = "게시글 리스트")
    List<PostSummary> posts;

}

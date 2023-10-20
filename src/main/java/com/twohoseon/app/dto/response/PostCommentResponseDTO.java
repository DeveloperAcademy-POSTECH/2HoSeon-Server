package com.twohoseon.app.dto.response;

import com.twohoseon.app.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentResponseDTO
 * @date : 2023/10/20
 * @modifyed : $
 **/

@Getter
@Builder
public class PostCommentResponseDTO {
    private Long postId;
    private Member member;
    private List<CommentInfoDTO> commentInfoDTOList;
}

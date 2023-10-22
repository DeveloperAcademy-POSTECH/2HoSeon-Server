package com.twohoseon.app.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentInfoDTO
 * @date : 2023/10/20
 * @modifyed : $
 **/

@Getter
@Builder
public class CommentInfoDTO {
    private Long id;
    private String profileImage;
    private String author;
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    private List<CommentInfoDTO> childComments;
}
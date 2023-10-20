package com.twohoseon.app.dto.request;

import lombok.Getter;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentRequestDTO
 * @date : 2023/10/18
 * @modifyed : $
 **/

@Getter
public class PostCommentRequestDTO {
    private String content;
    private Long parentId;
    private Long postId;
}

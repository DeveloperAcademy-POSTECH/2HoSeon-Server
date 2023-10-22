package com.twohoseon.app.repository.search;

import com.twohoseon.app.dto.response.PostInfoDTO;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchRepository
 * @date : 2023/10/22
 * @modifyed : $
 **/
public interface SearchRepository {
    List<PostInfoDTO> findAllPostsByKeyword(String keyword);
}

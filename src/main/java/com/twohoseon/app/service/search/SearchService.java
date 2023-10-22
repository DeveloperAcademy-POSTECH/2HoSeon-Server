package com.twohoseon.app.service.search;

import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.service.CommonService;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchService
 * @date : 2023/10/22
 * @modifyed : $
 **/
public interface SearchService extends CommonService {
    @Transactional
    List<PostInfoDTO> getSearchByKeyword(String keyword);
}

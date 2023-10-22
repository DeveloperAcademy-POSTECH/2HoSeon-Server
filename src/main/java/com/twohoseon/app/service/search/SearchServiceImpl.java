package com.twohoseon.app.service.search;

import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.repository.search.SearchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SearchServiceImpl
 * @date : 2023/10/22
 * @modifyed : $
 **/

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    @Transactional
    public List<PostInfoDTO> getSearchByKeyword(Pageable pageable, String keyword) {
        return searchRepository.findAllPostsByKeyword(pageable, keyword);
    }

}

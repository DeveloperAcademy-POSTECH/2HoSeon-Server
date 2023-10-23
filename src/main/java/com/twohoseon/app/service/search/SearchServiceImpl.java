package com.twohoseon.app.service.search;

import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.repository.post.PostRepository;
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

    private final PostRepository postRepository;

    @Override
    @Transactional
    public List<PostInfoDTO> getSearchByKeyword(Pageable pageable, String keyword) {
        Member member = getMemberFromRequest();
        return postRepository.findAllPostsByKeyword(pageable, keyword, member.getId());
    }

}

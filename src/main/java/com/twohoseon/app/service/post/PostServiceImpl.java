package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.PostCreateRequestDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.enums.PostStatus;
import com.twohoseon.app.entity.post.vote.VoteRepository;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.exception.MemberNotFoundException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostServiceImpl
 * @date : 10/18/23 11:18 PM
 * @modifyed : $
 **/

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    @Override
    public void createPost(PostCreateRequestDTO postCreateRequestDTO) {

        Member author = getMemberFromRequest();
        Post post = Post.builder()
                .author(author)
                .postType(postCreateRequestDTO.getPostType())
                .title(postCreateRequestDTO.getTitle())
                .contents(postCreateRequestDTO.getContents())
                .image(postCreateRequestDTO.getImage())
                .externalURL(postCreateRequestDTO.getExternalURL())
                .postTagList(postCreateRequestDTO.getPostTagList())
                .postCategoryType(postCreateRequestDTO.getPostCategoryType())
                .build();
        postRepository.save(post);
    }

    @Override
    @Transactional
    public List<PostInfoDTO> fetchPosts(Pageable pageable, PostStatus postStatus) {
        Member member = getMemberFromRequest();
        return postRepository.findAllPosts(pageable, postStatus, member.getId());
    }

    @Override
    public PostInfoDTO fetchPost(Long postId) {
        Member member = getMemberFromRequest();
        PostInfoDTO postInfoDTO = postRepository.findPostById(postId, member.getId());
        return postInfoDTO;
    }

    @Override
    @Transactional
    public VoteCountsDTO createVote(Long postId, VoteType voteType) {
        String providerId = getProviderIdFromRequest();
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberNotFoundException());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        post.createVote(member, voteType);
        postRepository.save(post);
        return postRepository.getVoteInfo(postId);
    }


}

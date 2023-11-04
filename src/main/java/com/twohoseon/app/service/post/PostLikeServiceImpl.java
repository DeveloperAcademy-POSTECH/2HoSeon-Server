package com.twohoseon.app.service.post;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostLike;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostLikeRepository;
import com.twohoseon.app.repository.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostLikeServiceImpl
 * @date : 2023/10/18
 * @modifyed : $
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void likePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + providerId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found post id : " + postId));

        if (postLikeRepository.findByMemberAndPost(member, post).isPresent()) {
            throw new IllegalStateException("Already check post like");
        }

        post.incrementLikeCount();
        postRepository.save(post);

        PostLike postLike = PostLike.builder()
                .member(member)
                .post(post)
                .build();

        postLikeRepository.save(postLike);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + providerId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found post id : " + postId));

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new NotFoundException("Could not found post-like id"));

        post.decrementLike();
        postRepository.save(post);

        postLikeRepository.delete(postLike);
    }
}

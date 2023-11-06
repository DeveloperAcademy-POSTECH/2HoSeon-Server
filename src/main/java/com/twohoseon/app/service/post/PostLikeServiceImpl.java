package com.twohoseon.app.service.post;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostLike;
import com.twohoseon.app.exception.LikeExistsException;
import com.twohoseon.app.exception.LikeNotCheckException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.post.PostLikeRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final PostRepository postRepository;

    @Override
    public void likePost(Long postId) {
        Member member = getMemberFromRequest();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        if (postLikeRepository.findByMemberAndPost(member, post).isPresent()) {
            throw new LikeExistsException();
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
    public void unlikePost(Long postId) {
        Member member = getMemberFromRequest();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new LikeNotCheckException());

        post.decrementLike();
        postRepository.save(post);

        postLikeRepository.delete(postLike);
    }
}

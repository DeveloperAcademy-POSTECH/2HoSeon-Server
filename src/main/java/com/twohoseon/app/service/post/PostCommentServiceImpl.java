package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.PostCommentRequestDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostComment;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostCommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostCommentServiceImpl
 * @date : 2023/10/18
 * @modifyed : $
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void commentCreate(PostCommentRequestDTO postCommentRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + providerId));

        Post post = postRepository.findById(postCommentRequestDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Could not found post id : " + postCommentRequestDTO.getPostId()));

        PostComment parentPostComment = null;

        if (postCommentRequestDTO.getParentId() != null) {
            parentPostComment = postCommentRepository.findById(postCommentRequestDTO.getParentId())
                    .orElseThrow(() -> new NotFoundException("Could not found post-comment id : " + postCommentRequestDTO.getParentId()));


            if (parentPostComment.getPost() != post) {

                throw new NotFoundException("Not equal id");
            }
        }

        PostComment postComment = PostComment
                .builder()
                .author(member)
                .post(post)
                .content(postCommentRequestDTO.getContent())
                .build();

        if (parentPostComment != null) {
            postComment.updateParent(parentPostComment);
        }

        postCommentRepository.save(postComment);

        if (parentPostComment != null) {
            parentPostComment.addChildComment(postComment);
            postCommentRepository.save(parentPostComment);
        }
    }

    @Override
    public List<PostComment> commentRead(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Could not found post id : " + postId));

        postCommentRepository.findPostCommentsByPost(post);

        return postCommentRepository.findPostCommentsByPost(post)
                .orElseThrow(() -> new NotFoundException("Not found comment at this post"));
    }

}

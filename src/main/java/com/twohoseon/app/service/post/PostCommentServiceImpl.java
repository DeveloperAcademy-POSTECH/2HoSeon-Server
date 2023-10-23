package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.PostCommentRequestDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostComment;
import com.twohoseon.app.exception.CommentNotFoundException;
import com.twohoseon.app.exception.MemberNotFoundException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostCommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

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
    @Transactional
    public void createComment(Long postId, PostCommentRequestDTO postCommentRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberNotFoundException("Could not found member id : " + providerId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Could not found post id : " + postId));

        PostComment parentPostComment = null;

        if (postCommentRequestDTO.getParentId() != null) {
            parentPostComment = postCommentRepository.findById(postCommentRequestDTO.getParentId())
                    .orElseThrow(() -> new CommentNotFoundException());


            if (parentPostComment.getPost() != post) {
                throw new NotFoundException("Not equal id");
            }
        }

        post.addComment();
        postRepository.save(post);

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
    @Transactional
    public void removeComment(Long postId, Long postCommentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException());

        if (postComment.getPost().getId() != post.getId()) {
            throw new IllegalStateException("Not equal post id");
        } else if (postComment.getAuthor().getProviderId() != getProviderIdFromRequest()) {
            throw new IllegalStateException("Not equal provider id");
        }

        postCommentRepository.delete(postComment);

        post.deleteComment();
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void updateComment(Long postId, Long postCommentId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Not found post id" + postId));

        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException("Not found post comment id" + postCommentId));

        if (postComment.getPost().getId() != post.getId()) {
            throw new IllegalStateException("Not equal post id");
        } else if (postComment.getAuthor().getProviderId() != getProviderIdFromRequest()) {
            throw new IllegalStateException("Not equal provider id");
        }

        postComment.updateContent(content);
        postCommentRepository.save(postComment);
    }
}

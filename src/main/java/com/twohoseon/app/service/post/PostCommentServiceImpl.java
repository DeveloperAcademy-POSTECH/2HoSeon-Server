package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.CommentCreateRequestDTO;
import com.twohoseon.app.dto.response.PostCommentInfoDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.PostComment;
import com.twohoseon.app.exception.CommentNotFoundException;
import com.twohoseon.app.exception.MemberNotFoundException;
import com.twohoseon.app.exception.PermissionDeniedException;
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
    @Transactional
    public void createComment(CommentCreateRequestDTO commentCreateRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String providerId = authentication.getName();

        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberNotFoundException());

        Post post = postRepository.findById(commentCreateRequestDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException());

        PostComment parentPostComment = null;

        if (commentCreateRequestDTO.getParentId() != null) {
            parentPostComment = postCommentRepository.findById(commentCreateRequestDTO.getParentId())
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
                .content(commentCreateRequestDTO.getContent())
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
    public void deleteComment(Long postCommentId) {
        //TODO 유저 권한 체크
        //TODO 삭제시 자식이 존재하는 경우 자식들도 사라져야함.
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException());

        if (postComment.getAuthor() != getMemberFromRequest()) {
            throw new PermissionDeniedException();
        }
        postComment.getPost().deleteComment();
        postCommentRepository.delete(postComment);
    }

    @Override
    @Transactional
    public void updateComment(Long postCommentId, String content) {
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException());

        if (postComment.getAuthor() != getMemberFromRequest()) {
            throw new PermissionDeniedException();
        }

        postComment.updateContent(content);
    }

    @Override
    public List<PostCommentInfoDTO> getPostCommentChildren(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        List<PostCommentInfoDTO> postCommentLists = postCommentRepository.findByPostAndId(post, commentId);
        return postCommentLists;
    }
}

package com.twohoseon.app.service.comment;

import com.twohoseon.app.dto.request.comment.CommentCreateRequestDTO;
import com.twohoseon.app.dto.request.comment.SubCommentCreateRequestDTO;
import com.twohoseon.app.dto.response.CommentInfoDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.exception.CommentNotFoundException;
import com.twohoseon.app.exception.PermissionDeniedException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createComment(CommentCreateRequestDTO commentCreateRequestDTO) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(commentCreateRequestDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException());
        Comment comment = Comment
                .builder()
                .author(member)
                .post(post)
                .content(commentCreateRequestDTO.getContents())
                .build();
        commentRepository.save(comment);
        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendPostCommentNotification(post, member.getNickname(), false);
            } catch (ExecutionException | InterruptedException e) {
                log.debug("sendPostCommentNotification error: ", e);
            }
        });
    }

    @Override
    @Transactional
    public void createSubComment(Long commentId, SubCommentCreateRequestDTO subCommentCreateRequestDTO) {
        Member member = getMemberFromRequest();

        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());

        Comment subComment = Comment.builder()
                .parentComment(parentComment)
                .content(subCommentCreateRequestDTO.getContents())
                .author(member)
                .post(parentComment.getPost())
                .build();
        commentRepository.save(subComment);
    }


    @Override
    @Transactional
    public void deleteComment(Long postCommentId) {
        Member member = getMemberFromRequest();
        Comment comment = commentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException());
        if (comment.getAuthor() != member) {
            throw new PermissionDeniedException();
        }
        comment.getPost().decrementCommentCount();
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long postCommentId, String content) {
        Member member = getMemberFromRequest();
        Comment comment = commentRepository.findById(postCommentId)
                .orElseThrow(() -> new CommentNotFoundException());
        if (comment.getAuthor() != member) {
            throw new PermissionDeniedException();
        }

        comment.updateContent(content);
    }

    @Override
    public List<CommentInfoDTO> getPostComments(Long postId) {
        return commentRepository.getAllCommentsFromPost(postId);
    }

    @Override
    public List<CommentInfoDTO> getSubComments(Long commentId) {
        return commentRepository.getSubComments(commentId);
    }
}

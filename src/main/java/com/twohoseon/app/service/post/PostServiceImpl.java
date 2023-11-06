package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.entity.post.vote.VoteRepository;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.exception.PermissionDeniedException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.exception.ReviewExistException;
import com.twohoseon.app.repository.member.MemberRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.notification.NotificationService;
import com.twohoseon.app.service.schedule.JobSchedulingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
@Slf4j
public class PostServiceImpl implements PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final JobSchedulingService jobSchedulingService;
    private final NotificationService notificationService;

    @Override
    public void createPost(PostRequestDTO postRequestDTO) {
        //TODO 이미지 추가를 위해 PostCreateRequestDTO 수정 및 Request Type 변경 필요(json to form-data)
        Member author = getMemberFromRequest();
        Post post = Post.builder()
                .author(author)
                .visibilityScope(postRequestDTO.getVisibilityScope())
                .title(postRequestDTO.getTitle())
                .contents(postRequestDTO.getContents())
                .price(postRequestDTO.getPrice())
                .externalURL(postRequestDTO.getExternalURL())
                .build();
        postRepository.save(post);
        try {
            jobSchedulingService.schedulePostExpireJob(post.getId());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
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
    public void updatePost(Long postId, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        Member author = post.getAuthor();
        Member member = getMemberFromRequest();
        if (!author.equals(member))
            throw new PermissionDeniedException();

        post.updatePost(postRequestDTO);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        Member author = post.getAuthor();
        Member member = getMemberFromRequest();
        if (!author.equals(member))
            throw new PermissionDeniedException();
        postRepository.delete(post);
    }

    @Override
    public void createReview(Long postId, ReviewRequestDTO reviewRequestDTO) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();
        if (post.isReviewExist()) {
            throw new ReviewExistException();
        }
        post.createReview(reviewRequestDTO);
        postRepository.save(post);
        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendPostReviewNotification(post);
            } catch (ExecutionException | InterruptedException e) {
                log.debug("sendPostReviewNotification error: ", e);
            }
        });
    }

    @Override
    public void updateReview(Long postId, ReviewRequestDTO reviewRequestDTO) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();
        post.updateReview(reviewRequestDTO);
    }

    @Override
    @Transactional
    public void deleteReview(Long postId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();

        postRepository.delete(post.deleteReview());
    }

    @Override
    public void subscribePost(Long postId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        post.subscribe(member);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public VoteCountsDTO createVote(Long postId, VoteType voteType) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        post.createVote(member, voteType);
        postRepository.save(post);
        return postRepository.getVoteInfo(postId);
    }


}

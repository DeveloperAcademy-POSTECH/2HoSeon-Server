package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostCreateRequestDTO;
import com.twohoseon.app.dto.request.post.PostUpdateRequestDTO;
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
import com.twohoseon.app.service.schedule.JobSchedulingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
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
    private final JobSchedulingService jobSchedulingService;

    @Override
    public void createPost(PostCreateRequestDTO postCreateRequestDTO) {

        Member author = getMemberFromRequest();
        Post post = Post.builder()
                .author(author)
                .visibilityScope(postCreateRequestDTO.getVisibilityScope())
                .title(postCreateRequestDTO.getTitle())
                .contents(postCreateRequestDTO.getContents())
                .externalURL(postCreateRequestDTO.getExternalURL())
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
    public void updatePost(Long postId, PostUpdateRequestDTO postUpdateRequestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        Member author = post.getAuthor();
        Member member = getMemberFromRequest();
        if (!author.equals(member))
            throw new PermissionDeniedException();

        post.updatePost(postUpdateRequestDTO);
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

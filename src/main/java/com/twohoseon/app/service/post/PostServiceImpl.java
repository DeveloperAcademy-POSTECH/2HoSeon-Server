package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.dto.response.PostInfoDTO;
import com.twohoseon.app.dto.response.VoteCountsDTO;
import com.twohoseon.app.dto.response.post.PostSummary;
import com.twohoseon.app.dto.response.post.ReviewDetail;
import com.twohoseon.app.dto.response.post.ReviewFetch;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.exception.PermissionDeniedException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.exception.ReviewExistException;
import com.twohoseon.app.exception.VoteExistException;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.image.ImageService;
import com.twohoseon.app.service.notification.NotificationService;
import com.twohoseon.app.service.schedule.JobSchedulingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final PostRepository postRepository;
    private final JobSchedulingService jobSchedulingService;
    private final NotificationService notificationService;
    private final ImageService imageService;

    @Override
    public void createPost(PostRequestDTO postRequestDTO, MultipartFile file) {
        Member author = getMemberFromRequest();

        String image = null;
        if (!file.isEmpty()) {
            image = imageService.uploadImage(file, "posts");
        }

        Post post = Post.builder()
                .author(author)
                .visibilityScope(postRequestDTO.getVisibilityScope())
                .title(postRequestDTO.getTitle())
                .contents(postRequestDTO.getContents())
                .image(image)
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
    public List<PostInfoDTO> fetchPosts(Pageable pageable, VisibilityScope visibilityScope) {
        Member member = getMemberFromRequest();
        return postRepository.findAllPosts(pageable, member, visibilityScope);
    }

    @Override
    public PostInfoDTO fetchPost(Long postId) {
        Member member = getMemberFromRequest();
        PostInfoDTO postInfoDTO = postRepository.findPostById(postId, member.getId());
        return postInfoDTO;
    }

    @Override
    @Transactional
    public void updatePost(Long postId, PostRequestDTO postRequestDTO, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        Member author = post.getAuthor();
        Member member = getMemberFromRequest();
        if (!author.equals(member))
            throw new PermissionDeniedException();

        String image = null;
        if (!file.isEmpty()) {
            image = imageService.updateImage(file, "posts", postId);
        }
        post.updatePost(postRequestDTO, image);
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
    public void createReview(Long postId, ReviewRequestDTO reviewRequestDTO, MultipartFile file) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();
        if (post.isReviewExist()) {
            throw new ReviewExistException();
        }
        String image = imageService.uploadImage(file, "reviews");

        post.createReview(reviewRequestDTO, image);
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
    public void updateReview(Long postId, ReviewRequestDTO reviewRequestDTO, MultipartFile file) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();

        String image = null;
        if (file != null && file.isEmpty()) {
            image = imageService.updateImage(file, "reviews", post.getReview().getId());
        }

        post.updateReview(reviewRequestDTO, image);
        postRepository.save(post);
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
    public void subscribePost(Long postId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        post.subscribe(member);
    }

    @Override
    public ReviewFetch fetchReviews(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType) {
        Member reqMember = getMemberFromRequest();
        ConsumerType consumerType = reqMember.getConsumerType();
        List<PostSummary> recentReviews = postRepository.findRecentReviews(visibilityScope, reqMember, reviewType, consumerType);
        List<PostSummary> reviews = postRepository.findReviews(pageable, reqMember, visibilityScope, reviewType);
        return ReviewFetch.builder()
                .recentReviews(recentReviews)
                .reviewType(reviewType)
                .reviews(reviews)
                .build();
    }

    @Override
    public ReviewDetail getReviewDetail(Long postId) {
        Member member = getMemberFromRequest();
        PostSummary originalPost = postRepository.getPostSummaryInReviewDetail(postId);
        PostInfoDTO reviewPost = postRepository.getReviewDetailByPostId(postId);
        return ReviewDetail.builder()
                .originalPost(originalPost)
                .reviewPost(reviewPost)
                .isMine(originalPost.getAuthor().getId().equals(member.getId()))
                .build();
    }

    @Override
    @Transactional
    public VoteCountsDTO createVote(Long postId, VoteType voteType) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (post.hasVoteFromMember(member)) {
            throw new VoteExistException();
        }
        post.createVote(member, voteType);
        postRepository.save(post);
        return postRepository.getVoteInfo(postId);
    }


}

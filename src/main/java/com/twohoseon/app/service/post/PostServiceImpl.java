package com.twohoseon.app.service.post;

import com.twohoseon.app.dto.request.post.PostRequest;
import com.twohoseon.app.dto.request.review.ReviewRequest;
import com.twohoseon.app.dto.response.VoteCounts;
import com.twohoseon.app.dto.response.mypage.MypageFetch;
import com.twohoseon.app.dto.response.post.*;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.ReviewType;
import com.twohoseon.app.enums.mypage.MyVoteCategoryType;
import com.twohoseon.app.enums.post.VisibilityScope;
import com.twohoseon.app.exception.PermissionDeniedException;
import com.twohoseon.app.exception.PostNotFoundException;
import com.twohoseon.app.exception.ReviewExistException;
import com.twohoseon.app.exception.VoteExistException;
import com.twohoseon.app.repository.comment.CommentRepository;
import com.twohoseon.app.repository.post.PostRepository;
import com.twohoseon.app.service.image.ImageService;
import com.twohoseon.app.service.notification.NotificationService;
import com.twohoseon.app.service.schedule.JobSchedulingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.data.domain.PageRequest;
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
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JobSchedulingService jobSchedulingService;
    private final NotificationService notificationService;
    private final ImageService imageService;

    @Override
    public void createPost(PostRequest postRequest, MultipartFile file) {
        Member author = getMemberFromRequest();

        String image = null;
        if (file != null && !file.isEmpty()) {
            image = imageService.uploadImage(file, "posts");
        }

        Post post = Post.builder()
                .author(author)
                .visibilityScope(postRequest.getVisibilityScope())
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .image(image)
                .price(postRequest.getPrice())
                .externalURL(postRequest.getExternalURL())
                .build();
        postRepository.save(post);
        try {
            jobSchedulingService.schedulePostExpireJob(post.getId());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void completeVote(Long postId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthor().equals(member))
            throw new PermissionDeniedException();

        try {
            jobSchedulingService.deleteScheduledPostExpireJob(postId);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        post.setPostToComplete();
        postRepository.save(post);
        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendPostExpiredNotification(post);
            } catch (ExecutionException | InterruptedException e) {
                log.debug("sendPostReviewNotification error: ", e);
            }
        });

    }

    @Override
    @Transactional
    public List<PostInfo> fetchPosts(Pageable pageable, VisibilityScope visibilityScope) {
        Member member = getMemberFromRequest();
        return postRepository.findAllPosts(pageable, member, visibilityScope);
    }

    @Override
    public PostDetail fetchPostDetail(Long postId) {
        Member member = getMemberFromRequest();
        PostDetail postDetail = postRepository.findPostDetailById(postId, member.getId());
        return postDetail;
    }

    //TODO 업로드 수정
    @Override
    @Transactional
    public void updatePost(Long postId, PostRequest postRequest, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        Member author = post.getAuthor();
        Member member = getMemberFromRequest();
        if (!author.equals(member))
            throw new PermissionDeniedException();

        String image = null;

        if (file != null && !file.isEmpty()) {
            if (post.getImage() == null) {
                image = imageService.uploadImage(file, "posts");
            } else {
                imageService.deleteImage(post.getImage().toString(), "posts");
                image = imageService.uploadImage(file, "posts");
            }
        }
        post.updatePost(postRequest, image);
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
        if (post.getReview() != null) {
            commentRepository.deleteByPost(post.getReview());
            postRepository.delete(post.getReview());
        }

        commentRepository.deleteByPost(post);
        postRepository.delete(post);
    }

    @Override
    public void createReview(Long postId, ReviewRequest reviewRequest, MultipartFile file) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();
        if (post.isReviewExist()) {
            throw new ReviewExistException();
        }
        if (reviewRequest.getIsPurchased() == true && file.isEmpty() && file == null) {
            throw new RuntimeException();
        }

        String image = null;
        if (file != null && !file.isEmpty()) {
            image = imageService.uploadImage(file, "reviews");
        }

        post.createReview(reviewRequest, image);
        postRepository.save(post);
        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendPostReviewNotification(post);
            } catch (ExecutionException | InterruptedException e) {
                log.debug("sendPostReviewNotification error: ", e);
            }
        });
    }

    //TODO 업로드 수정
    @Override
    public void updateReview(Long postId, ReviewRequest reviewRequest, MultipartFile file) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (!post.isAuthor(member))
            throw new PermissionDeniedException();
        if (reviewRequest.getIsPurchased() == true && post.getReview().getImage() == null && file == null && file.isEmpty()) {
            throw new RuntimeException();
        }

        String image = null;
        if (file != null && !file.isEmpty()) {
            if (post.getReview().getImage() == null) {
                image = imageService.uploadImage(file, "reviews");
            } else {
                imageService.deleteImage(post.getReview().getImage().toString(), "reviews");
                image = imageService.uploadImage(file, "reviews");
            }
        }

        post.updateReview(reviewRequest, image);
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
        post.deleteReview();
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
    @Transactional
    public void unsubscribePost(Long postId) {
        Member reqMember = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        post.unsubscribe(reqMember);
    }

//    @Override
//    public ReviewFetch fetchReviewsTemp(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType) {
//        Member reqMember = getMemberFromRequest();
//        ConsumerType consumerType = reqMember.getConsumerType();
//        List<PostSummary> recentReviews = postRepository.findRecentReviews(visibilityScope, reqMember, reviewType, consumerType);
//        List<PostSummary> reviews = postRepository.findReviews(pageable, reqMember, visibilityScope, reviewType);
//        return AllReviewFetch.builder()
//                .recentReviews(recentReviews)
//                .reviewType(reviewType)
//                .reviews(reviews)
//                .build();
//    }

    @Override
    public AllReviewFetch fetchAllReviews(VisibilityScope visibilityScope) {
        Member reqMember = getMemberFromRequest();
        ConsumerType consumerType = reqMember.getConsumerType();
        Pageable pageable = PageRequest.of(0, 5);
        List<PostSummary> recentReviews = postRepository.findRecentReviews(visibilityScope, reqMember, consumerType);
        List<PostSummary> allReviews = postRepository.findReviews(pageable, reqMember, visibilityScope, ReviewType.ALL);
        List<PostSummary> purchasedReviews = postRepository.findReviews(pageable, reqMember, visibilityScope, ReviewType.PURCHASED);
        List<PostSummary> notPurchasedReviews = postRepository.findReviews(pageable, reqMember, visibilityScope, ReviewType.NOT_PURCHASED);
        return AllReviewFetch.builder()
                .myConsumerType(consumerType)
                .recentReviews(recentReviews)
                .allReviews(allReviews)
                .purchasedReviews(purchasedReviews)
                .notPurchasedReviews(notPurchasedReviews)
                .build();
    }

    @Override
    public List<PostSummary> fetchReviews(VisibilityScope visibilityScope, Pageable pageable, ReviewType reviewType) {
        List<PostSummary> reviews = postRepository.findReviews(pageable, getMemberFromRequest(), visibilityScope, reviewType);
        return reviews;
    }

    @Override
    public MypageFetch fetchMyReviews(VisibilityScope visibilityScope, Pageable pageable) {
        Member reqMember = getMemberFromRequest();
        List<PostSummary> reviews = postRepository.findReviewsById(pageable, reqMember, visibilityScope);

        return MypageFetch.builder()
                .total(postRepository.getTotalReviewCount(reqMember, visibilityScope))
                .posts(reviews)
                .build();
    }

    @Override
    public ReviewDetail fetchReviewDetail(Long postId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        PostSummary originalPost;
        PostInfo reviewPost;
        String commentPreview = null;
        String commentPreviewImage = null;
        Integer commentCount;
        originalPost = postRepository.getPostSummaryInReviewDetail(postId);
        reviewPost = postRepository.getReviewDetailByPostId(post.getReview().getId());
        commentCount = postRepository.calculateCommentCountByPostId(reviewPost.getPostId());
        if (commentCount != null) {
            commentPreview = postRepository.getCommentPreviewByPostId(reviewPost.getPostId());
            commentPreviewImage = postRepository.getCommentPreviewImageByPostId(reviewPost.getPostId());
        }
        ReviewDetail reviewDetail = new ReviewDetail(originalPost, reviewPost, originalPost.getAuthor().getId().equals(member.getId()), commentCount, commentPreview, commentPreviewImage);
        return reviewDetail;
    }

    @Override
    public ReviewDetail fetchReviewDetailByReviewId(Long reviewId) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findPostByReviewId(reviewId)
                .orElseThrow(PostNotFoundException::new);

        PostSummary originalPost;
        PostInfo reviewPost;
        String commentPreview = null;
        String commentPreviewImage = null;
        Integer commentCount;
        originalPost = postRepository.getPostSummaryInReviewDetail(post.getId());
        reviewPost = postRepository.getReviewDetailByPostId(post.getReview().getId());
        commentCount = postRepository.calculateCommentCountByPostId(reviewPost.getPostId());
        if (commentCount != null) {
            commentPreview = postRepository.getCommentPreviewByPostId(reviewPost.getPostId());
            commentPreviewImage = postRepository.getCommentPreviewImageByPostId(reviewPost.getPostId());
        }
        ReviewDetail reviewDetail = new ReviewDetail(originalPost, reviewPost, originalPost.getAuthor().getId().equals(member.getId()), commentCount, commentPreview, commentPreviewImage);

        return reviewDetail;
    }

    @Override
    public MypageFetch fetchMypagePosts(Pageable pageable, MyVoteCategoryType myVoteCategoryType) {
        Member reqMember = getMemberFromRequest();
        long total;
        List<PostSummary> posts;
        total = postRepository.countAllPostsByMyVoteCategoryType(reqMember, myVoteCategoryType);
        posts = postRepository.findAllPostsByMyVoteCategoryType(pageable, reqMember, myVoteCategoryType);
        MypageFetch mypageFetch = MypageFetch.builder()
                .total(total)
                .posts(posts)
                .build();

        return mypageFetch;
    }


    @Override
    @Transactional
    public VoteCounts createVote(Long postId, boolean myChoice) {
        Member member = getMemberFromRequest();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if (post.hasVoteFromMember(member)) {
            throw new VoteExistException();
        }
        post.createVote(member, myChoice);
        postRepository.save(post);
        return postRepository.getVoteInfo(postId);
    }
}

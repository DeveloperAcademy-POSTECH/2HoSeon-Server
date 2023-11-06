package com.twohoseon.app.entity.post;

import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.dto.request.post.PostRequestDTO;
import com.twohoseon.app.dto.request.review.ReviewRequestDTO;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.vote.Vote;
import com.twohoseon.app.entity.post.vote.VoteId;
import com.twohoseon.app.enums.VoteType;
import com.twohoseon.app.enums.post.PostStatus;
import com.twohoseon.app.enums.post.VisibilityScope;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : Post
 * @date : 10/17/23 4:01 PM
 * @modifyed : $
 **/
@Entity
@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @Comment("작성자")
    protected Member author;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("공개 범위")
    private VisibilityScope visibilityScope;

    @NotNull
    @Column
    @Comment("제목")
    private String title;

    @Nullable
    @Column
    @Comment("텍스트 내용")
    private String contents;

    @Nullable
    @Column
    @Comment("제품 가격")
    private Integer price;

    @NotNull
    @Column
    @Comment("포스트 상태")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus postStatus = PostStatus.ACTIVE;

    @NotNull
    @Column
    @Comment("조회 수")
    @Builder.Default
    private Integer viewCount = 0;

    @NotNull
    @Column
    @Comment("댓글 수")
    @Builder.Default
    private Integer commentCount = 0;
    @Nullable
    @Column
    @Comment("외부 링크")
    private String externalURL;

    @NotNull
    @Column
    @Comment("좋아요 수")
    @Builder.Default
    private Integer likeCount = 0;

    @NotNull
    @Column
    @Comment("투표 찬성 수")
    @Builder.Default
    private Integer agreeCount = 0;

    @NotNull
    @Column
    @Comment("투표 반대 수")
    @Builder.Default
    private Integer disagreeCount = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Comment("이미지 리스트")
    @Builder.Default
    private List<String> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "id.post", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    @Comment("투표 리스트")
    private Set<Vote> votes = new LinkedHashSet<>();

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    @Builder.Default
    @Comment("리뷰")
    private Post review = null;

    //샀다 안샀다 boolean
    @Column
    @Builder.Default
    @Comment("구매 여부")
    private boolean isPurchased = false;

    @Column
    @Comment("투표 결과")
    @Builder.Default
    private VoteResult voteResult = VoteResult.DRAW;

    @ManyToMany
    @JoinTable(name = "Review_subscribed_members",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "members_id"))
    @Builder.Default
    @Comment("구독자 리스트")
    private Set<Member> subscribers = new LinkedHashSet<>();

    public void setSubscribers(Set<Member> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean isAuthor(Member member) {
        return this.author.equals(member);
    }

    //review is exist
    public boolean isReviewExist() {
        return this.review != null;
    }

    public void setPostToComplete() {
        this.postStatus = PostStatus.CLOSED;
        if (agreeCount > disagreeCount)
            this.voteResult = VoteResult.BUY;
        else if (agreeCount < disagreeCount)
            this.voteResult = VoteResult.NOT_BUY;
        else
            this.voteResult = VoteResult.DRAW;
    }

    public void incrementLikeCount() {
        this.likeCount += 1;
    }

    public void decrementLike() {
        this.likeCount -= 1;
    }

    public void createVote(Member voter, VoteType voteType) {
        Vote vote = Vote.builder()
                .id(VoteId.builder()
                        .voter(voter)
                        .post(this)
                        .build())
                .isAgree(voteType == VoteType.AGREE)
                .build();
        if (voteType == VoteType.AGREE)
            this.agreeCount += 1;
        else
            this.disagreeCount += 1;
        this.votes.add(vote);
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        this.commentCount--;
    }

    //For Post Update
    public void updatePost(PostRequestDTO postRequestDTO) {
        //TODO 이미지 부분 List<String>으로 변경
        if (postRequestDTO.getTitle() != null)
            this.title = postRequestDTO.getTitle();
        if (postRequestDTO.getContents() != null)
            this.contents = postRequestDTO.getContents();
        if (postRequestDTO.getExternalURL() != null)
            this.externalURL = postRequestDTO.getExternalURL();
        if (postRequestDTO.getImage() != null)
            this.imageList.add(postRequestDTO.getImage());
        if (postRequestDTO.getVisibilityScope() != null)
            this.visibilityScope = postRequestDTO.getVisibilityScope();
    }

    private void updatePost(ReviewRequestDTO reviewRequestDTO) {
        if (reviewRequestDTO.getTitle() != null)
            this.title = reviewRequestDTO.getTitle();
        if (reviewRequestDTO.getContents() != null)
            this.contents = reviewRequestDTO.getContents();
        //TODO 멀티파트 처리후 이미지 링크 저장 필
//        if (reviewRequestDTO.getImage() != null)
//            this.imageList.add(reviewRequestDTO.getImage());
    }

    public void createReview(ReviewRequestDTO reviewRequestDTO) {
        Post review = Post.builder()
                .postStatus(PostStatus.REVIEW)
                .author(this.author)
                .visibilityScope(this.visibilityScope)
                .title(reviewRequestDTO.getTitle())
                .contents(reviewRequestDTO.getContents())
                .isPurchased(reviewRequestDTO.isPurchased())
                .build();
        this.review = review;
    }

    public void updateReview(ReviewRequestDTO reviewRequestDTO) {
        this.review.updatePost(reviewRequestDTO);
    }

    public Post deleteReview() {
        Post review = this.review;
        this.review = null;
        return review;
    }

    public void subscribe(Member member) {
        subscribers.add(member);
    }

    public int getVoteCount() {
        return this.agreeCount + this.disagreeCount;
    }

}

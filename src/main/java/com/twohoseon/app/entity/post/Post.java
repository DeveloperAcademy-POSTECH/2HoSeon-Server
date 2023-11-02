package com.twohoseon.app.entity.post;

import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.enums.PostType;
import com.twohoseon.app.entity.post.vote.Vote;
import com.twohoseon.app.entity.post.vote.VoteId;
import com.twohoseon.app.enums.PostCategoryType;
import com.twohoseon.app.enums.VoteType;
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
    private Member author;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("게시글 타입")
    private PostType postType;

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
    @Comment("이미지")
    private String image;

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
    @Comment("조회수")
    @Builder.Default
    private Integer viewCount = 0;

    @NotNull
    @Column
    @Comment("댓글 수")
    @Builder.Default
    private Integer commentCount = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Comment("태그 리스트")
    @Builder.Default
    private List<String> postTagList = new ArrayList<>();

    @OneToMany(mappedBy = "id.post", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Vote> votes = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column
    @Comment("게시글 카테고리 타입")
    private PostCategoryType postCategoryType;

    public void setAuthor(Member author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addLike() {
        this.likeCount += 1;
    }

    public void addComment() {
        this.commentCount += 1;
    }

    public void cancelLike() {
        int restLikeCount = this.likeCount - 1;
        if (restLikeCount < 0) {
            throw new IllegalStateException("Don't cancel post like");
        }
        this.likeCount = restLikeCount;
    }

    public void deleteComment() {
        int restCommentCount = this.commentCount - 1;
        if (restCommentCount < 0) {
            throw new IllegalStateException("Don't cancel post comment");
        }
        this.commentCount = restCommentCount;
    }

    public void createVote(Member voter, VoteType voteType) {
        Vote vote = Vote.builder()
                .id(VoteId.builder()
                        .voter(voter)
                        .post(this)
                        .build())
                .isAgree(voteType == VoteType.AGREE)
                .gender(voter.getUserGender())
                .grade(voter.getGrade())
                .regionType(voter.getSchool().getSchoolRegion())
                .schoolType(voter.getSchool().getSchoolType())
                .grade(voter.getGrade())
                .build();
        this.votes.add(vote);
    }
}

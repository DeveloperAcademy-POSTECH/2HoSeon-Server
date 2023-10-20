package com.twohoseon.app.entity.post;

import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.enums.PostStatus;
import com.twohoseon.app.entity.post.enums.PostType;
import com.twohoseon.app.entity.post.vote.Vote;
import com.twohoseon.app.entity.post.vote.VoteId;
import com.twohoseon.app.enums.VoteType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
    @Enumerated
    @Column
    @Comment("게시글 상태")
    @Builder.Default
    private PostStatus postStatus = PostStatus.ACTIVE;

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

    public void setAuthor(Member author) {
        this.author = author;
    }

    protected Post() {
    }

    public void createVote(Member voter, VoteType voteType) {
        Vote vote = Vote.builder()
                .id(VoteId.builder()
                        .voter(voter)
                        .post(this)
                        .build())
                .isAgree(voteType == VoteType.AGREE)
                .build();
        this.votes.add(vote);
    }
}

package com.twohoseon.app.entity.post;

import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.Member;
import com.twohoseon.app.entity.post.enums.PostStatus;
import com.twohoseon.app.entity.post.enums.PostType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @Comment("작성자")
    private Member author;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("게시글 타입")
    private PostType postType;

    @NotNull
    @Enumerated
    @Column(length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    @Comment("게시글 상태")
    private PostStatus postStatus;

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
    private Integer likeCount = 0;

    @NotNull
    @Column
    @Comment("조회수")
    private Integer viewCount = 0;

    @NotNull
    @Column
    @Comment("댓글 수")
    private Integer commentCount = 0;

    @ElementCollection
    @Column
    @Comment("태그 리스트")
    private List<String> postTagList = new ArrayList<>();


    public void setAuthor(Member author) {
        this.author = author;
    }
}

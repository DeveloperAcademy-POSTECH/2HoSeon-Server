package com.twohoseon.app.entity.post.vote;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ConsumerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "VOTE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"voter_id", "post_id"})
})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    @Column
    Boolean isAgree;

    @Column
    @Enumerated(EnumType.STRING)
    ConsumerType consumerType;


    protected Vote() {
    }
}
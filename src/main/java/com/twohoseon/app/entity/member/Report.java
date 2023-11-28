package com.twohoseon.app.entity.member;

import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.post.Comment;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ReportCategory;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : Report
 * @date : 11/27/23 8:26 PM
 * @modifyed : $
 **/
@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member reportedMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReportCategory reason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

}

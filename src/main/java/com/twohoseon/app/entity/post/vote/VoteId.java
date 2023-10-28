package com.twohoseon.app.entity.post.vote;

import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.entity.post.Post;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteId
 * @date : 10/19/23 3:28 PM
 * @modifyed : $
 **/
@Embeddable
@Builder
@AllArgsConstructor
@Getter
//@EqualsAndHashCode
public class VoteId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteId)) return false;
        VoteId voteId = (VoteId) o;
        return post.equals(voteId.post) && voter.equals(voteId.voter);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    protected VoteId() {
    }
    // getters, setters, equals, hashCode 메서드가 필요합니다.
}


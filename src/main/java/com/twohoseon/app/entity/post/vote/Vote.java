package com.twohoseon.app.entity.post.vote;

import com.twohoseon.app.entity.post.enums.Region;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
@Builder
@AllArgsConstructor
public class Vote {

    @EmbeddedId
    VoteId id;

    @Column
    Boolean isAgree;

    @Column
    Region region;

    @Column
    String gender;


    protected Vote() {
    }
}
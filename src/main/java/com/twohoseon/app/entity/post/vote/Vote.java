package com.twohoseon.app.entity.post.vote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.twohoseon.app.enums.ConsumerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
@Builder
@AllArgsConstructor
public class Vote {

    @JsonIgnoreProperties
    @EmbeddedId
    VoteId id;

    @Column
    Boolean isAgree;

    @Column
    @Enumerated(EnumType.STRING)
    ConsumerType consumerType;

    protected Vote() {
    }
}
package com.twohoseon.app.entity.post.vote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.twohoseon.app.enums.ConsumerType;
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

    @JsonIgnoreProperties
    @EmbeddedId
    VoteId id;

    @Column
    Boolean isAgree;

    @Column
    ConsumerType consumerType;

    protected Vote() {
    }
}
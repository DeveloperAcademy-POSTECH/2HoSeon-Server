package com.twohoseon.app.entity.post.vote;

import com.twohoseon.app.enums.GenderType;
import com.twohoseon.app.enums.RegionType;
import com.twohoseon.app.enums.SchoolType;
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
    GenderType gender;

    @Column
    Boolean isAgree;

    @Column
    SchoolType schoolType;

    @Column
    Integer grade;

    @Column
    RegionType regionType;


//    @Column


    protected Vote() {
    }
}
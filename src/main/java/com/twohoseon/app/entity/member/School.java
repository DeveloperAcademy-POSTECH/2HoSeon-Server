package com.twohoseon.app.entity.member;

import com.twohoseon.app.enums.RegionType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@Embeddable
@EqualsAndHashCode(of = {"schoolName", "schoolRegion"})
public class School {

    private String schoolName;

    @Enumerated(EnumType.STRING)
    private RegionType schoolRegion;

    protected School() {
    }

    public School(String schoolName, RegionType schoolRegion) {
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
    }
}
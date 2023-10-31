package com.twohoseon.app.entity.member;

import com.twohoseon.app.enums.RegionType;
import com.twohoseon.app.enums.SchoolType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;


@Getter
@Embeddable
public class School {

    private String schoolName;

    @Enumerated(EnumType.STRING)
    private RegionType schoolRegion;
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    protected School() {
    }

    public School(String schoolName, RegionType schoolRegion, SchoolType schoolType) {
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
        this.schoolType = schoolType;
    }
}
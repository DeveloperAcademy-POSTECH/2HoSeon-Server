package com.twohoseon.app.entity.member;

import com.twohoseon.app.enums.RegionType;
import com.twohoseon.app.enums.SchoolType;
import jakarta.persistence.Embeddable;
import lombok.Getter;


@Getter
@Embeddable
public class School {

    private String schoolName;

    private RegionType schoolRegion;

    private SchoolType schoolType;

    protected School() {
    }

    public School(String schoolName, RegionType schoolRegion, SchoolType schoolType) {
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
        this.schoolType = schoolType;
    }
}
package com.twohoseon.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class School {

    private String schoolName;

    private String schoolRegion;

    private String schoolType;

    protected School() {
    }

    public School(String schoolName, String schoolRegion, String schoolType) {
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
        this.schoolType = schoolType;
    }
}
package com.twohoseon.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class School {

    private String schoolName;

    private String schoolRegion;

    protected School() {}

    public School(String schoolName, String schoolRegion) {
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
    }
}
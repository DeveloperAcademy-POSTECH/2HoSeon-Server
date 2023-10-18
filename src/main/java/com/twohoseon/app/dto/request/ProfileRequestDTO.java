package com.twohoseon.app.dto.request;

import com.twohoseon.app.entity.School;
import lombok.Getter;

@Getter
public class ProfileRequestDTO {
    private String userProfileImage;
    private String userNickname;
    private School school;
    private Integer grade;
}

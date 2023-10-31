package com.twohoseon.app.entity.member;

import com.google.common.base.Objects;
import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.GenderType;
import com.twohoseon.app.enums.OAuth2Provider;
import com.twohoseon.app.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : Member
 * @date : 2023/10/07 1:28 PM
 * @modifyed : $
 **/

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OAuth2Provider provider;

    @Column(length = 50, nullable = false, unique = true)
    private String providerId;

    @Column(nullable = true)
    private String userEmail;
    @Column(nullable = true)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private GenderType userGender;

    @Column(nullable = true)
    private String userNickname;

    @Column(nullable = true)
    private String userProfileImage;

    @Column(nullable = true)
    private LocalDate userBirth;

    @Embedded
    @Column(nullable = true)
    private School school;

    @Column(nullable = true)
    private Integer grade;

    @Column(nullable = true)
    private String deviceToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private UserRole role;

    @Builder.Default
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Post> posts = new LinkedHashSet<>();

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

//    @OneToMany(mappedBy = "member", orphanRemoval = true)
//    private Set<PostComment> postComments = new LinkedHashSet<>();
//
//    public void setPostComments(Set<PostComment> postComments) {
//        this.postComments = postComments;
//    }

    public void updateAdditionalUserInfo(String userProfileImage, String userNickname, GenderType userGender, School school, Integer grade) {
        this.userProfileImage = userProfileImage;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.school = school;
        this.grade = grade;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    protected Member() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equal(id, member.id) && provider == member.provider
                && Objects.equal(providerId, member.providerId) && role == member.role;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, provider, providerId, role);
    }


}

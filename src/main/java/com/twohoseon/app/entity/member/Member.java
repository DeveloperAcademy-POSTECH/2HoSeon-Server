package com.twohoseon.app.entity.member;

import com.google.common.base.Objects;
import com.twohoseon.app.common.BaseTimeEntity;
import com.twohoseon.app.entity.post.Post;
import com.twohoseon.app.enums.ConsumerType;
import com.twohoseon.app.enums.OAuth2Provider;
import com.twohoseon.app.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
    private String nickname;

    @Column(nullable = true)
    private String profileImage;

    @Column
    @Enumerated(EnumType.STRING)
    private ConsumerType consumerType;

    @Embedded
    @Column(nullable = true)
    private School school;

    @Column(nullable = true)
    private String deviceToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private UserRole role;

    @Builder.Default
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Post> posts = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void updateAdditionalUserInfo(String profileImage, String nickname, School school) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.school = school;
    }


    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setUserProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

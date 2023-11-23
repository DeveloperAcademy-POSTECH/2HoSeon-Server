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

    //학교 등록 마지막 일
    @Column
    private LocalDate lastSchoolRegisterDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OAuth2Provider provider;

    @Column(length = 50, nullable = false, unique = true)
    private String providerId;

    @Column(nullable = true, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String profileImage;

    @Column
    @Enumerated(EnumType.STRING)
    private ConsumerType consumerType;

    @Embedded
    @Column(nullable = true)
    private School school;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private UserRole role;

    @Builder.Default
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Post> posts = new LinkedHashSet<>();

    @Column
    private String appleRefreshToken;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    @JoinTable(name = "Member_Block",
            joinColumns = @JoinColumn(name = "blocker_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id"))
    private Set<Member> blockedMember = new LinkedHashSet<>();

    public void updateAdditionalUserInfo(String profileImage, String nickname, School school) {
        if (profileImage != null)
            this.profileImage = profileImage;
        if (nickname != null)
            this.nickname = nickname;
        if (school != null)
            this.school = school;
        lastSchoolRegisterDate = LocalDate.now();
    }
    

    public boolean isSchoolRegisterable() {
        return lastSchoolRegisterDate == null || lastSchoolRegisterDate.isBefore(LocalDate.now().minusMonths(6));
    }

    public void setConsumerType(ConsumerType consumerType) {
        this.consumerType = consumerType;
    }

    protected Member() {
    }

    public void setAppleRefreshToken(String appleRefreshToken) {
        this.appleRefreshToken = appleRefreshToken;
    }

    public String getAppleRefreshToken() {
        return appleRefreshToken;
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


    public void unBlockedMember(Member blockedMember) {
        this.blockedMember.remove(blockedMember);
    }

    public void blockedMember(Member blockedMember) {
        this.blockedMember.add(blockedMember);
    }
}

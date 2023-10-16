package com.twohoseon.app.entity;

import com.google.common.base.Objects;
import com.twohoseon.app.enums.OAuth2Provider;
import com.twohoseon.app.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

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
public class Member {

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

    @Column(nullable = true)
    private String userNickname;

    @Column(nullable = true)
    private String userPhone;

    @Column(nullable = true)
    private String userProfileImage;

    @Column(nullable = true)
    private LocalDate userBirth;

    @Embedded
    @Column(nullable = true)
    private School school;

    @Column(nullable = true)
    private Integer grade;


    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private UserRole role;

    protected Member() {
    }

    public Member(OAuth2Provider provider, String providerId, String userEmail, String userName) {

        this(null, provider, providerId, UserRole.ROLE_USER, userEmail, userName);
    }

    private Member(Long id, OAuth2Provider provider, String providerId, UserRole role, String userEmail, String userName) {
        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.userEmail = userEmail;
        this.userName = userName;
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

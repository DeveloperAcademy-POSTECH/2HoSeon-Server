package com.twohoseon.app.security.oauth2.user;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.twohoseon.app.common.Id;
import com.twohoseon.app.entity.member.Member;
import com.twohoseon.app.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomOAuth2User
 * @date : 2023/10/07 2:43 PM
 * @modifyed : $
 **/
public class CustomOAuth2User implements OAuth2User, Serializable {

    private final Id<Member, Long> id;

    private final UserRole role;

    private final OAuth2User oAuth2User;

    private final String providerId;

    public CustomOAuth2User(Long id, UserRole role, String providerId, OAuth2User oAuth2User) {
        Preconditions.checkArgument(id != null, "id must be provided.");
        Preconditions.checkArgument(role != null, "role must be provided.");
        Preconditions.checkArgument(oAuth2User != null, "oAuth2User must be provided.");
        Preconditions.checkArgument(providerId != null, "providerId must be provided.");
        this.id = Id.of(Member.class, id);
        this.role = role;
        this.providerId = providerId;
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return this.providerId;
    }

    public Id<Member, Long> getId() {
        return this.id;
    }

    public UserRole getRole() {
        return this.role;
    }

    public OAuth2User getOAuth2User() {
        return oAuth2User;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomOAuth2User that = (CustomOAuth2User) o;
        return Objects.equal(id, that.id) && role == that.role
                && Objects.equal(oAuth2User, that.oAuth2User);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, role, oAuth2User);
    }
}
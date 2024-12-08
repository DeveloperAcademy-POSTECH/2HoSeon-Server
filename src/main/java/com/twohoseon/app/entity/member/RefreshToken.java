package com.twohoseon.app.entity.member;

import com.twohoseon.app.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RefreshToken
 * @date : 2023/10/07 4:11 PM
 * @modifyed : $
 **/
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime accessTokenExpirationTime;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpirationTime;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    boolean isBanned;

    public RefreshToken updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void banRefreshToken() {
        this.isBanned = true;
    }


}
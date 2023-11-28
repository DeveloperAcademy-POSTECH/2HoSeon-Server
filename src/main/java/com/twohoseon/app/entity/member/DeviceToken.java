package com.twohoseon.app.entity.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : DeviceToken
 * @date : 11/13/23 8:59 PM
 * @modifyed : $
 **/
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String token;

    public void setMember(Member member) {
        this.member = member;
    }
}

package com.twohoseon.app.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : BanList
 * @date : 11/28/23 1:09 AM
 * @modifyed : $
 **/
@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BanList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String providerId;

}

package com.back.domain.member.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;
}

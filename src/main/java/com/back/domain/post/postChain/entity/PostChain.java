package com.back.domain.post.postChain.entity;

import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class PostChain extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;
    private String title;

    public PostChain(Member author, String title) {
        this.author = author;
        this.title = title;
    }
}
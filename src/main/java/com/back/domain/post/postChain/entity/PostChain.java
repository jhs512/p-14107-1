package com.back.domain.post.postChain.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.postChainItem.entity.PostChainItem;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class PostChain extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;
    private String title;
    @OneToMany(mappedBy = "postChain", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<PostChainItem> items = new ArrayList<>();

    public PostChain(Member author, String title) {
        this.author = author;
        this.title = title;
    }

    public void addItem(Post post) {
        PostChainItem postChainItem = new PostChainItem(this, post, genLastItemNo());

        items.add(postChainItem);
    }

    private int genLastItemNo() {
        return items.size() + 1;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
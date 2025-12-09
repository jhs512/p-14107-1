package com.back.domain.post.postChain.repository;

import com.back.domain.post.postChain.entity.PostChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostChainRepository extends JpaRepository<PostChain, Integer> {
}
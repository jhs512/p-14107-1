package com.back.domain.post.postChain.service;

import com.back.domain.member.member.entity.Member;
import com.back.domain.post.postChain.entity.PostChain;
import com.back.domain.post.postChain.repository.PostChainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostChainService {
    private final PostChainRepository postChainRepository;

    public long count() {
        return postChainRepository.count();
    }

    public PostChain make(Member author, String subject) {
        PostChain postChain = new PostChain(author, subject);

        return postChainRepository.save(postChain);
    }
}

package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public long count() {
        return memberRepository.count();
    }

    public Member join(String username, String password, String nickname) {
        memberRepository.findByUsername(username).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        });

        Member member = new Member(username, password, nickname);

        return memberRepository.save(member);
    }
}

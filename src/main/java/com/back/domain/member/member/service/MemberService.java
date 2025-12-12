package com.back.domain.member.member.service;

import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.market.cart.service.CartService;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.global.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CartService cartService;
    private final WalletService walletService;

    public MemberService(
            MemberRepository memberRepository,
            CartService cartService,
            WalletService walletService) {
        this.memberRepository = memberRepository;
        this.cartService = cartService;
        this.walletService = walletService;
    }

    public long count() {
        return memberRepository.count();
    }

    public Member join(String username, String password, String nickname) {
        findByUsername(username).ifPresent(m -> {
            throw new DomainException("409-1", "이미 존재하는 username 입니다.");
        });

        Member member = memberRepository.save(new Member(username, password, nickname));

        cartService.make(member);
        walletService.make(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}

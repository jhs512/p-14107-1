package com.back.global.initData;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
public class BaseInitData {
    private final BaseInitData self;
    private final MemberService memberService;

    public BaseInitData(@Lazy BaseInitData self, MemberService memberService) {
        this.self = self;
        this.memberService = memberService;
    }

    @Bean
    public ApplicationRunner baseInitDataRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member systemMember = memberService.join("system", "1234", "시스템");
        Member bankMember = memberService.join("holding", "1234", "홀딩");
        Member adminMember = memberService.join("admin", "1234", "관리자");
        Member user1Member = memberService.join("user1", "1234", "유저1");
        Member user2Member = memberService.join("user2", "1234", "유저2");
        Member user3Member = memberService.join("user3", "1234", "유저3");
    }
}

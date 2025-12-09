package com.back.global.initData;

import com.back.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BaseInitData {
    private final MemberService memberService;

    @Bean
    public ApplicationRunner baseInitDataRunner() {
        return args -> {
            work1();
        };
    }

    private void work1() {
        log.debug("회원 수 {}", memberService.count());
    }
}

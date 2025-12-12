package com.back.domain.cash.cashLog.entity;

import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class CashLog extends BaseEntity {
    public enum EventType {
        충전__무통장입금,
        충전__토스페이먼츠,
        출금__통장입금,
        사용__주문결제,
        임시보관__주문결제,
    }

    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String relTypeCode;
    private int relId;
    @ManyToOne(fetch = LAZY)
    private Member member;
    @ManyToOne(fetch = LAZY)
    private Wallet wallet;
    private long amount;
    private long balance;

    public CashLog(EventType eventType, String relTypeCode, int relId, Member member, Wallet wallet, long amount, long balance) {
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.member = member;
        this.wallet = wallet;
        this.amount = amount;
        this.balance = balance;
    }
}
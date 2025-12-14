package com.back.domain.payout.payoutItem.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.payout.payout.entity.Payout;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class PayoutItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Payout payout;
    @Enumerated(EnumType.STRING)
    private PayoutEventType eventType;
    String relTypeCode;
    private int relId;
    private LocalDateTime payDate;
    @ManyToOne(fetch = LAZY)
    private Member payer;
    @ManyToOne(fetch = LAZY)
    private Member payee;
    private int amount;

    public PayoutItem(Payout payout, PayoutEventType eventType, String relTypeCode, int relId, LocalDateTime payDate, Member payer, Member payee, int amount) {
        this.payout = payout;
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.payDate = payDate;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }
}

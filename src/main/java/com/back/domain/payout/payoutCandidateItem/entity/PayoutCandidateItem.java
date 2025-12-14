package com.back.domain.payout.payoutCandidateItem.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.domain.payout.payoutItem.entity.PayoutItem;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class PayoutCandidateItem extends BaseEntity {
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
    @OneToOne(fetch = LAZY)
    @Setter
    private PayoutItem payoutItem;

    public PayoutCandidateItem(PayoutEventType eventType, String relTypeCode, int relId, LocalDateTime payDate, Member payer, Member payee, int amount) {
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.payDate = payDate;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }
}

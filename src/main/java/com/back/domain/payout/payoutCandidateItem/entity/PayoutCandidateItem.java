package com.back.domain.payout.payoutCandidateItem.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class PayoutCandidateItem extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private PayoutEventType eventType;
    String relTypeCode;
    int relId;
    @ManyToOne(fetch = LAZY)
    Member payer;
    @ManyToOne(fetch = LAZY)
    Member payee;
    int amount;

    public PayoutCandidateItem(PayoutEventType eventType, String relTypeCode, int relId, Member payer, Member payee, int amount) {
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }
}

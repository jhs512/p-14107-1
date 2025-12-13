package com.back.domain.payout.payout.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.domain.payout.payoutItem.entity.PayoutItem;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class Payout extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member payee;
    private LocalDateTime payoutDate;
    private int amount;

    @OneToMany(mappedBy = "payout", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<PayoutItem> items = new ArrayList<>();

    public Payout(Member payee) {
        this.payee = payee;
    }

    public PayoutItem addItem(PayoutEventType eventType, String relTypeCode, int relId, LocalDateTime payDate, Member payer, Member payee, int amount) {
        PayoutItem payoutItem = new PayoutItem(
                this, eventType, relTypeCode, relId, payDate, payer, payee, amount
        );

        items.add(payoutItem);

        this.amount += amount;

        return payoutItem;
    }
}

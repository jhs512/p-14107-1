package com.back.domain.cash.wallet.entity;

import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class Wallet extends BaseEntity {
    @OneToOne(fetch = LAZY)
    private Member holder;

    @Getter
    private long balance;

    @OneToMany(mappedBy = "wallet", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<CashLog> cashLogs = new ArrayList<>();

    public Wallet(Member holder) {
        this.holder = holder;
    }

    public boolean hasBalance() {
        return balance > 0;
    }

    public void credit(long amount, CashLog.EventType eventType) {
        balance += amount;

        addCashLog(amount, eventType);
    }

    private CashLog addCashLog(long amount, CashLog.EventType eventType) {
        CashLog cashLog = new CashLog(
                eventType,
                holder.getModelTypeCode(),
                holder.getId(),
                holder,
                this,
                amount,
                balance
        );

        cashLogs.add(cashLog);

        return cashLog;
    }
}
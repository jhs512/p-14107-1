package com.back.domain.payout.payout.service;

import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.market.order.entity.Order;
import com.back.domain.market.orderItem.entity.OrderItem;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.domain.payout.payoutCandidateItem.entity.PayoutCandidateItem;
import com.back.domain.payout.payoutCandidateItem.repository.PayoutCandidateItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayoutService {
    private final PayoutCandidateItemRepository payoutCandidateItemRepository;
    private final MemberService memberService;

    public void makePayoutCandidateItems(Order order) {
        order.getItems().forEach(this::makePayoutCandidateItems);
    }

    private void makePayoutCandidateItems(OrderItem orderItem) {
        int amount = orderItem.getSalePrice();
        int fee = (int) (amount * 0.1);
        int rest = amount - fee;

        makePayoutCandidateItem(
                PayoutEventType.정산__상품판매_수수료,
                orderItem.getModelTypeCode(),
                orderItem.getId(),
                orderItem.getOrder().getPayDate(),
                orderItem.getBuyer(),
                memberService.findSystem().get(),
                fee
        );

        makePayoutCandidateItem(
                PayoutEventType.정산__상품판매_대금,
                orderItem.getModelTypeCode(),
                orderItem.getId(),
                orderItem.getOrder().getPayDate(),
                orderItem.getBuyer(),
                orderItem.getProduct().getAuthor(),
                rest
        );
    }

    private void makePayoutCandidateItem(
            PayoutEventType eventType,
            String relTypeCode,
            int relId,
            LocalDateTime payDate,
            Member payer,
            Member payee,
            int amount
    ) {
        PayoutCandidateItem payoutCandidateItem = new PayoutCandidateItem(
                eventType,
                relTypeCode,
                relId,
                payDate,
                payer,
                payee,
                amount
        );

        payoutCandidateItemRepository.save(payoutCandidateItem);
    }

    public List<PayoutCandidateItem> findPayoutCandidatesDue(int limit) {
        LocalDateTime fourteenDaysAgo = LocalDateTime
                .now()
                .minusDays(-1)
                .toLocalDate()
                .atStartOfDay();

        return payoutCandidateItemRepository.findByPayoutItemIsNullAndPayDateBefore(
                fourteenDaysAgo,
                PageRequest.of(0, limit)
        );
    }
}

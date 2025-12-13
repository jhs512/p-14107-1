package com.back.domain.payout.payout.service;

import com.back.domain.market.order.entity.Order;
import com.back.domain.market.orderItem.entity.OrderItem;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.domain.payout.payoutCandidateItem.entity.PayoutCandidateItem;
import com.back.domain.payout.payoutCandidateItem.repository.PayoutCandidateItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                orderItem.getBuyer(),
                memberService.findSystem().get(),
                fee
        );

        makePayoutCandidateItem(
                PayoutEventType.정산__상품판매_대금,
                orderItem.getModelTypeCode(),
                orderItem.getId(),
                orderItem.getBuyer(),
                orderItem.getProduct().getAuthor(),
                rest
        );
    }

    private void makePayoutCandidateItem(
            PayoutEventType eventType,
            String relTypeCode,
            int relId,
            Member payer,
            Member payee,
            int amount
    ) {
        PayoutCandidateItem payoutCandidateItem = new PayoutCandidateItem(
                eventType,
                relTypeCode,
                relId,
                payer,
                payee,
                amount
        );

        payoutCandidateItemRepository.save(payoutCandidateItem);
    }
}

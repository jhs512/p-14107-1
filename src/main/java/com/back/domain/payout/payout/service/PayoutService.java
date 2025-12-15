package com.back.domain.payout.payout.service;

import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.market.order.entity.Order;
import com.back.domain.market.orderItem.entity.OrderItem;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.payout.payout.entity.Payout;
import com.back.domain.payout.payout.repository.PayoutRepository;
import com.back.domain.payout.payout.type.PayoutEventType;
import com.back.domain.payout.payoutCandidateItem.entity.PayoutCandidateItem;
import com.back.domain.payout.payoutCandidateItem.repository.PayoutCandidateItemRepository;
import com.back.domain.payout.payoutItem.entity.PayoutItem;
import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayoutService {
    private final PayoutCandidateItemRepository payoutCandidateItemRepository;
    private final MemberService memberService;
    private final PayoutRepository payoutRepository;
    private final WalletService walletService;

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

        return payoutCandidateItemRepository.findByPayoutItemIsNullAndPayDateBeforeOrderByPayee(
                fourteenDaysAgo,
                PageRequest.of(0, limit)
        );
    }

    public RsData<Integer> makeDuePayouts(int limit) {
        List<PayoutCandidateItem> items = findPayoutCandidatesDue(limit);

        items.stream()
                .collect(Collectors.groupingBy(PayoutCandidateItem::getPayee))
                .forEach((payee, candidateItems) -> {
                    Payout payout = findActiveByPayee(payee).get();

                    candidateItems.forEach(item -> {
                        PayoutItem payoutItem = payout.addItem(
                                item.getEventType(),
                                item.getRelTypeCode(),
                                item.getRelId(),
                                item.getPayDate(),
                                item.getPayer(),
                                item.getPayee(),
                                item.getAmount()
                        );

                        item.setPayoutItem(payoutItem);
                    });
                });

        return new RsData<>(
                !items.isEmpty() ? "201-1" : "200-2",
                !items.isEmpty() ? "생성할 정산데이터가 없습니다." : "%d건의 정산데이터가 생성되었습니다.".formatted(items.size()),
                items.size()
        );
    }

    public Payout make(Member member) {
        Payout payout = new Payout(member);

        return payoutRepository.save(payout);
    }

    public Optional<Payout> findActiveByPayee(Member payee) {
        return payoutRepository.findByPayeeAndPayoutDateIsNull(payee);
    }

    public List<Payout> findActivePayouts(int limit) {
        return payoutRepository.findByPayoutDateIsNullAndAmountGreaterThan(0, PageRequest.of(0, limit));
    }

    public RsData<Integer> makeDuePayoutsComplete(int limit) {
        List<Payout> duePayouts = findActivePayouts(limit);

        duePayouts.forEach(payout -> {
            payout.setPayoutDate(LocalDateTime.now());

            Wallet holdingWallet = walletService.findHollding().get();
            Wallet payeeWallet = walletService.findByHolder(payout.getPayee()).get();

            if (payeeWallet.isSystem()) {
                holdingWallet.debit(payout.getAmount(), CashLog.EventType.정산지급__상품판매_수수료);
                payeeWallet.credit(payout.getAmount(), CashLog.EventType.정산수령__상품판매_수수료);
            } else {
                holdingWallet.debit(payout.getAmount(), CashLog.EventType.정산지급__상품판매_대금);
                payeeWallet.credit(payout.getAmount(), CashLog.EventType.정산수령__상품판매_대금);
            }
        });

        return new RsData<>(
                duePayouts.isEmpty() ? "200-1" : "201-1",
                duePayouts.isEmpty() ? "정산받을 회원이 없습니다." : "%d명의 회원이 정산받았습니다.".formatted(duePayouts.size()),
                duePayouts.size()
        );
    }
}

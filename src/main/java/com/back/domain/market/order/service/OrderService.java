package com.back.domain.market.order.service;

import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.market.cart.entity.Cart;
import com.back.domain.market.order.entity.Order;
import com.back.domain.market.order.repository.OrderRepository;
import com.back.domain.payout.payout.service.PayoutService;
import com.back.global.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final PayoutService payoutService;

    public Order make(Cart cart) {
        Wallet wallet = walletService.findByHolder(cart.getBuyer()).get();

        Order order = new Order(
                cart.getBuyer(),
                wallet,
                0,
                0
        );

        order.addItemsFrom(cart.getItems());

        cart.clear();

        return orderRepository.save(order);
    }

    public long count() {
        return orderRepository.count();
    }

    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    public void completePayment(Order order) {
        Wallet buyerWallet = walletService.findByHolder(order.getBuyer()).get();
        Wallet hollingWallet = walletService.findHollding().get();

        int salePrice = order.getSalePrice();

        if (buyerWallet.getBalance() < salePrice) {
            throw new DomainException("400-1", "잔액이 부족합니다.");
        }

        order.completePayment();

        buyerWallet.debit(salePrice, CashLog.EventType.사용__주문결제, order);
        hollingWallet.credit(salePrice, CashLog.EventType.임시보관__주문결제, order);

        payoutService.makePayoutCandidateItems(order);
    }
}
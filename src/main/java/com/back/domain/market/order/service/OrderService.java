package com.back.domain.market.order.service;

import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.market.cart.entity.Cart;
import com.back.domain.market.order.entity.Order;
import com.back.domain.market.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletService walletService;

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
}
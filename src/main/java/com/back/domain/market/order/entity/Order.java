package com.back.domain.market.order.entity;

import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.market.cartItem.entity.CartItem;
import com.back.domain.market.orderItem.entity.OrderItem;
import com.back.domain.market.product.entity.Product;
import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
public class Order extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member buyer;
    @ManyToOne(fetch = LAZY)
    private Wallet wallet;
    private int price;
    private int salePrice;

    @OneToMany(mappedBy = "order", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(Member buyer, Wallet wallet, int price, int salePrice) {
        this.buyer = buyer;
        this.wallet = wallet;
        this.price = price;
        this.salePrice = salePrice;
    }

    public void addItemsFrom(List<CartItem> items) {
        items
                .forEach(item -> {
                    addItem(item.getBuyer(), item.getProduct());
                });
    }

    public void addItem(Member buyer, Product product) {
        OrderItem orderItem = new OrderItem(
                this,
                buyer,
                product,
                product.getName(),
                product.getPrice(),
                product.getSalePrice()
        );

        items.add(orderItem);

        price += product.getPrice();
        salePrice += product.getSalePrice();
    }
}
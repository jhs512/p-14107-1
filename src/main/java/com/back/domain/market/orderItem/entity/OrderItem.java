package com.back.domain.market.orderItem.entity;

import com.back.domain.market.order.entity.Order;
import com.back.domain.market.product.entity.Product;
import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    @ManyToOne(fetch = LAZY)
    private Product product;

    private String productName;

    private int price;

    private int salePrice;

    public OrderItem(Order order, Member buyer, Product product, String productName, int price, int salePrice) {
        this.order = order;
        this.buyer = buyer;
        this.product = product;
        this.productName = productName;
        this.price = price;
        this.salePrice = salePrice;
    }
}
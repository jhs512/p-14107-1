package com.back.domain.market.cart.entity;

import com.back.domain.market.cartItem.entity.CartItem;
import com.back.domain.market.product.entity.Product;
import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@NoArgsConstructor
@Getter
public class Cart extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private Member buyer;

    @OneToMany(mappedBy = "cart", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Getter
    private int itemCount;

    public Cart(Member buyer) {
        this.buyer = buyer;
    }

    public CartItem addItem(Product product) {
        CartItem cartItem = new CartItem(this, buyer, product);

        items.add(cartItem);
        itemCount++;

        return cartItem;
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public void clear() {
        items.clear();
        itemCount = 0;
    }
}
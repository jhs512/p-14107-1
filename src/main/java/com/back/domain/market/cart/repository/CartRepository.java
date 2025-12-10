package com.back.domain.market.cart.repository;

import com.back.domain.market.cart.entity.Cart;
import com.back.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByBuyer(Member buyer);
}
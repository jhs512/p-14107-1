package com.back.domain.cash.wallet.repository;

import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByHolder(Member holder);
}

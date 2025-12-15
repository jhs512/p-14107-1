package com.back.domain.payout.payout.repository;

import com.back.domain.member.member.entity.Member;
import com.back.domain.payout.payout.entity.Payout;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    Optional<Payout> findByPayeeAndPayoutDateIsNull(Member payee);

    List<Payout> findByPayoutDateIsNullAndAmountGreaterThan(int amount, PageRequest pageable);
}

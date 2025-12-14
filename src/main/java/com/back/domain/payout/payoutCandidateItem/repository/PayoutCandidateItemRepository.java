package com.back.domain.payout.payoutCandidateItem.repository;

import com.back.domain.payout.payoutCandidateItem.entity.PayoutCandidateItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PayoutCandidateItemRepository extends JpaRepository<PayoutCandidateItem, Integer> {
    List<PayoutCandidateItem> findByPayoutItemIsNullAndPayDateBeforeOrderByPayee(LocalDateTime date, Pageable pageable);
}

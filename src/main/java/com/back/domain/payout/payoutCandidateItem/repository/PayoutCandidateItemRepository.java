package com.back.domain.payout.payoutCandidateItem.repository;

import com.back.domain.payout.payoutCandidateItem.entity.PayoutCandidateItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayoutCandidateItemRepository extends JpaRepository<PayoutCandidateItem, Integer> {
}

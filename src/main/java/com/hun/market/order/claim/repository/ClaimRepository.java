package com.hun.market.order.claim.repository;

import com.hun.market.order.claim.domain.Claim;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByMemberId(Long memberId);
}

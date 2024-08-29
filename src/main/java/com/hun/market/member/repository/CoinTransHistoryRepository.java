package com.hun.market.member.repository;

import com.hun.market.member.domain.CoinTransHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinTransHistoryRepository extends JpaRepository<CoinTransHistory, Long> {

    List<CoinTransHistory> findByMemberId(Long memberId);

    List<CoinTransHistory> findByMemberIdOrderByEventDateDesc(Long memberId);
}

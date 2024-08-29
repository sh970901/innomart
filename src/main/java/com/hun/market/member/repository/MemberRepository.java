package com.hun.market.member.repository;

import com.hun.market.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMbName(String mbName);

    Optional<Member> findByMbEmail(String mbEmail);

    Optional<Member> findById(Long memberId);

    List<Member> findAllByOrderById();

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.cart WHERE m.mbName = :name")
    Optional<Member> findByMbNameWithCart(@Param("name") String name);


}

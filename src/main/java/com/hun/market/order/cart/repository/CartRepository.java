package com.hun.market.order.cart.repository;

import com.hun.market.member.domain.Member;
import com.hun.market.order.cart.domain.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
}

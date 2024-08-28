package com.hun.market.order.order.repository;

import com.hun.market.order.order.domain.OrderPoss;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPossRepository extends JpaRepository<OrderPoss, Long> {
    Optional<OrderPoss> findByIdAndOrderPossYn(Long id, String orderPossYn);
}

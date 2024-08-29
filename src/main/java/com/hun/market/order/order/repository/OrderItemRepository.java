package com.hun.market.order.order.repository;

import com.hun.market.order.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    OrderItem findByItemId(Long id);
}

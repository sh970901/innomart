package com.hun.market.order.order.repository;

import com.hun.market.order.order.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.item WHERE o.buyer.id = :memberId")
    List<Order> findOrdersWithItemsByMemberId(@Param("memberId") Long memberId);

//    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.claim cl JOIN FETCH oi.item WHERE o.buyer.id = :memberId")
//    List<Order> findOrdersWithItemsByMemberId(@Param("memberId") Long memberId);

}

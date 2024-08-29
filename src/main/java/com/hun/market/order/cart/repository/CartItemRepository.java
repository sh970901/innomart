package com.hun.market.order.cart.repository;

import com.hun.market.order.cart.domain.Cart;
import com.hun.market.order.cart.domain.CartItem;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByCartOrderByItemAsc(Cart cart, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.id IN :ids")
    void deleteAllByIds(List<Long> ids);
}

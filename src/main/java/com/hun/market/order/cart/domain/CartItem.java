package com.hun.market.order.cart.domain;

import com.hun.market.item.domain.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    @Positive
    private Integer quantity;

    public static CartItem createByItem(Item item, Integer quantity) {
        return CartItem.builder()
                .item(item)
                .quantity(quantity)
                .build();
    }

    public void mappingCart(Cart cart) {
        this.cart = cart;
    }

    public void increaseQuantity(Integer quantity){
        this.quantity += quantity;
    }


    public void decreaseQuantity() {
        if (this.quantity <= 1) {
            // 장바구니에 담긴 수량이 1개인 경우, 아이템을 삭제하고 재고 차감
            deleteThis();
        } else {
            // 장바구니에 담긴 수량이 여러 개인 경우, 수량을 줄이고 재고 차감
            this.quantity -= 1;
        }
    }

    public void deleteThis(){
        this.cart.removeCartItem(this); // 카트에서 아이템 삭제
        this.cart = null;
    }
}

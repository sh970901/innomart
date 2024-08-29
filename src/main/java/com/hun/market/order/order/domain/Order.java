package com.hun.market.order.order.domain;

import com.hun.market.base.entity.BaseEntity;
import com.hun.market.member.domain.Member;
import com.hun.market.order.pay.domain.Payment;
import com.hun.market.order.ship.domain.ShippingAddress;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member buyer;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "shipping_street"))
    })
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();


    public static Order createByMember(List<OrderItem> orderItems, Member buyer) {

        Order order = Order.builder()
                .buyer(buyer)
                .orderItems(orderItems)
                .orderStatus(OrderStatus.PENDING)
                .shippingAddress(ShippingAddress.builder().street("Innople").build())
                .totalPrice(0L)
                .build();

        order.calcOrderTotalPrice();

        orderItems.forEach(orderItem -> orderItem.mappingOrder(order));

        return order;
    }

    private void calcOrderTotalPrice() {
        this.totalPrice = orderItems.stream()
                                    .mapToLong(OrderItem::calcOrderItemTotalPrice)
                                    .sum();
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", buyer='" + buyer + '\'' +
            ", orderStatus='" + orderStatus + '\'' +
            ", totalPrice=" + totalPrice +
            ", shippingAddress=" + shippingAddress +
            ", orderItems=" + orderItems +
            '}';
    }

    public void complete() {
        orderStatus = OrderStatus.COMPLETE;
    }

    public String orderStatusDescription(){

        return "";
    }
}

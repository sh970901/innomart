package com.hun.market.order.order.event;

import com.hun.market.core.event.Events;
import com.hun.market.item.event.DecreaseItStockEvent;
import com.hun.market.member.event.MbDeductCoinEvent;
import com.hun.market.order.order.domain.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderCompletedEvent implements OrderEvent {
    private final Order order;

    public OrderCompletedEvent(Order order) {
        this.order = order;
    }

    @Override
    public void process() {

        Events.raise(new MbDeductCoinEvent(order)); // 멤버 코인 차감
        Events.raise(new DecreaseItStockEvent(order.getOrderItems())); // 상품 갯수 차감

        order.complete();
    }

    public Order getOrder() {
        return order;
    }
}

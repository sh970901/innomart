package com.hun.market.member.event;

import com.hun.market.member.domain.Member;
import com.hun.market.order.order.domain.Order;

public class MbDeductCoinEvent implements MemberEvent {
    private Order order;

    public MbDeductCoinEvent(Order order) {
        this.order = order;
    }

    @Override public void process() {
        Member member = order.getBuyer();
        member.deductCoin(order);
    }
}

package com.hun.market.letter.limiter.dto;

import com.hun.market.letter.limiter.context.LimiterContext;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "from")
public class LimiterResult {
    private Long order;
    private String message;

    public static LimiterResult pass(){
        return LimiterResult.from().message(LimiterContext.NO_ORDER_MESSAGE).order(LimiterContext.NO_ORDER).build();
    }

    public static LimiterResult wait(Long order){
        return LimiterResult.from().message(LimiterContext.ORDER_MESSAGE).order(order).build();
    }
}

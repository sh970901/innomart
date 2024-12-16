package com.hun.market.letter.limiter.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebGateConfig {

    @Bean
    @ConditionalOnMissingBean(WebGate.class)
    public WebGate defaultWebGate(WebClient webClient) {
        return new DefaultWebGate(webClient);
    }

    @Bean(name = "webGateClient")
    public WebClient webGateClient(WebClient.Builder builder) {
        return builder.baseUrl("http://43.203.254.209:8082") // 기본 URL 설정
                .build();
    }

//    @Bean
//    @Primary
//    public WebGate defaultWebGate2(WebClient webClient) {
//        return new DefaultWebGate2(webClient);
//    }
}

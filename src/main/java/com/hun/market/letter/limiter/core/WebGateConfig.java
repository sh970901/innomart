package com.hun.market.letter.limiter.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

@Configuration
public class WebGateConfig {

    private static final int CONNECT_TIMEOUT_MILLIS = 1000;
    private static final String ACCESS_CONTROL_URL = "http://43.203.254.209:8082";

    @Bean
    @ConditionalOnMissingBean(WebGate.class)
    public WebGate defaultWebGate(WebClient webClient) {
        return new DefaultWebGate(webClient);
    }

    @Bean(name = "webGateClient")
    public WebClient webGateClient(WebClient.Builder builder) {

        TcpClient tcpClient = TcpClient.create()
                                       .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS);

        HttpClient httpClient = HttpClient.from(tcpClient);

        return builder
            .baseUrl(ACCESS_CONTROL_URL) // 기본 URL 설정
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

//    @Bean
//    @Primary
//    public WebGate defaultWebGate2(WebClient webClient) {
//        return new DefaultWebGate2(webClient);
//    }
}

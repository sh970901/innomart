package com.hun.market.letter.limiter.controller;

import com.hun.market.core.response.CommonResponse;
import com.hun.market.letter.limiter.dto.LimiterResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {
    private final WebClient webGateClient;

    @GetMapping("/limiter/order/{gateId}/{userId}")
    public Long proxyRequest(@PathVariable String gateId, @PathVariable String userId) {

         CommonResponse<Long> count = webGateClient.get()
                        .uri("/api/v1/limiter/order/{gateId}/{userId}", gateId, userId)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CommonResponse<Long>>() {})
                        .block();
//        System.out.println(count);

        if (count != null) {
            return count.getData();
        }
        return 0L;
    }
}

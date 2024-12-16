package com.hun.market.letter.limiter.core;


import com.hun.market.core.response.CommonResponse;
import com.hun.market.core.util.RandomStringGenerator;
import com.hun.market.letter.limiter.dto.LimiterResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
public class DefaultWebGate extends AbstractDefaultWebGate {
    private final WebClient webGateClient;
    public DefaultWebGate(WebClient webGateClient){
        this.webGateClient = webGateClient;
    }
    @Override
    public boolean WG_IsNeedToWaiting(HttpServletRequest request, HttpServletResponse response, String gateId){
        String userId = WG_ReadCookie(request, "userId");

        if (userId == null) {
            userId = WG_GetRandomString(10);
            WG_WriteCookie(response, "userId", userId);
        }

        CommonResponse<LimiterResult> result = WG_CallLimiterApi(gateId, userId);
        if (result == null) {
            log.error("ERROR GATE_ID");
            return false;
        }

        LimiterResult data = result.getData();
        return data.getOrder() != 0;
    }


    /**
     * @param gateId
     * @param userId
     * @return
     *
     * 통신 뭐가 좋을지 고민
     */
    @Override
    public CommonResponse<LimiterResult> WG_CallLimiterApi(String gateId, String userId) {
        return webGateClient.get()
                .uri("/api/v1/limiter/{gateId}/{userId}", gateId, userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonResponse<LimiterResult>>() {})
                .block();

    }

    private String generateUserId() {
        return RandomStringGenerator.generateRandomString(10);
    }
}

package com.hun.market.letter.limiter.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebGateManager {

    private final Map<String, WebGate> webGateRegistry;

    @Autowired
    public WebGateManager(List<WebGate> webGates) {
        webGateRegistry = new HashMap<>();
        for (WebGate webGate : webGates) {
            String gateName = webGate.getClass().getSimpleName();
            webGateRegistry.put(gateName, webGate);
        }
    }

    // 특정 조건에 따라 WebGate를 선택
    public WebGate getWebGate(String gateName) {
        WebGate webGate = webGateRegistry.get(gateName);
        if (webGate == null) {
            throw new IllegalArgumentException("No WebGate found for gateId: " + gateName);
        }
        return webGate;
    }

    // WebGateManager를 통해 WebGate 사용
    public boolean isNeedToWait(String gateName, String gateId, HttpServletRequest request, HttpServletResponse response) {
        WebGate webGate = getWebGate(gateName);
        return webGate.WG_IsNeedToWaiting(request, response, gateId);
    }
}

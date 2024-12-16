package com.hun.market.letter.limiter.core;


import com.hun.market.core.response.CommonResponse;
import com.hun.market.letter.limiter.dto.LimiterResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface WebGate {

    boolean WG_IsNeedToWaiting(HttpServletRequest request, HttpServletResponse response, String gateId);
    CommonResponse<LimiterResult> WG_CallLimiterApi(String gateId, String userId);

}

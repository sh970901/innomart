package com.hun.market.letter.limiter.core;


import com.hun.market.core.response.CommonResponse;
import com.hun.market.letter.limiter.dto.LimiterResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDefaultWebGate implements WebGate {

    @Override
    public abstract boolean WG_IsNeedToWaiting(HttpServletRequest request, HttpServletResponse response, String gateId);
    @Override
    public abstract CommonResponse<LimiterResult> WG_CallLimiterApi(String gateId, String userId);

    protected String WG_GetRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        SecureRandom random = new SecureRandom();

        String chars[] = "1,2,3,4,5,6,7,8,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z".split(",");

        for (int i = 0; i < length; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }
    /* 쿠키 저장*/
    protected void WG_WriteCookie(HttpServletResponse res, String key, String value) {
        // default cookie (auto domain)
        try {
            Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
            cookie.setMaxAge(86400 * 1);
            cookie.setPath("/");
            res.addCookie(cookie);
        } catch (Exception ex) {
            // skip
        }
    }

    protected String WG_ReadCookie(HttpServletRequest req, String key) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }
}

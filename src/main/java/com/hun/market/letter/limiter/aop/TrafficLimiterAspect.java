package com.hun.market.letter.limiter.aop;


import com.hun.market.letter.limiter.annotation.TrafficLimiter;
import com.hun.market.letter.limiter.core.WebGate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TrafficLimiterAspect {

    private final WebGate webGate;

    @Around(value = "@annotation(trafficLimiter)")
    public Object limitTraffic(ProceedingJoinPoint joinPoint, TrafficLimiter trafficLimiter) throws Throwable {
        final ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = Objects.requireNonNull(servletContainer).getRequest();
        final HttpServletResponse response = Objects.requireNonNull(servletContainer).getResponse();

        boolean needToWaiting = false;
        try{
            needToWaiting = webGate.WG_IsNeedToWaiting(request, response, trafficLimiter.gateId());
            log.info("####### Waiting: {}", needToWaiting);
        }
        catch (Exception e){
            log.error("####### 통신 실패: {}", e.getMessage());
        }

        if (needToWaiting) {
            Object[] args = joinPoint.getArgs(); // 매개변수 가져오기
            String code = null;
            Model model = null;

            // 매개변수에서 @RequestParam("code")와 Model 객체 찾기
            for (Object arg : args) {
                if (arg instanceof String && code == null) {
                    code = (String) arg; // code 값 저장
                } else if (arg instanceof Model) {
                    model = (Model) arg; // Model 객체 저장
                }
            }

            if (model != null && code != null) {
                model.addAttribute("code", code); // Model에 code 추가
                System.out.println("AOP: Model에 code 추가됨 - " + code);
            }
            return trafficLimiter.waitingPagePath();
        }

        return joinPoint.proceed();
    }

}

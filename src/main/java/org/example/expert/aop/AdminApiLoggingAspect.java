package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class AdminApiLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* org.example.expert.domain.*.controller.*Admin*.*(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {

        // 필요한 값 꺼내기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        Long userId = (Long) request.getAttribute("userId");
        String requestURI = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        // RequestBody 꺼내기 (메서드 파라미터에서)
        Object[] args = joinPoint.getArgs();

        // 실행 전 로깅
        log.info("어드민 API 접근 - userId: {}, 시각: {}, URL: {}, RequestBody: {}",
                userId, requestTime, requestURI, objectMapper.writeValueAsString(args));

        // 실제 메서드 실행
        Object result = joinPoint.proceed();

        // 실행 후 로깅
        log.info("어드민 API 응답 - ResponseBody: {}", result);

        // 결과 반환
        return result;
    }
}

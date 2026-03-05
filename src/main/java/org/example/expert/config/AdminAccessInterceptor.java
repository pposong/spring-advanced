package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        // 1. UserRole 꺼내기
        String userRole = (String) request.getAttribute("userRole");

        // 2. 어드민인지 확인
        if (!UserRole.ADMIN.name().equals(userRole)) {
            // 어드민 아니면 예외 던지기
            throw new AuthException("관리자 권한이 없습니다.");
        }

        // 3. 어드민이면 로깅
        // 현재 시각: LocalDateTime.now()
        // 요청 URL: request.getRequestURI()
        log.info("어드민 API 접근 - 시각: {}, URL: {}", LocalDateTime.now(), request.getRequestURI());
        return true;
    }
}

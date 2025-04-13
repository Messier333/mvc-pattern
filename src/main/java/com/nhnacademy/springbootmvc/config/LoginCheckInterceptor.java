package com.nhnacademy.springbootmvc.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.getBeanType().getSimpleName().equals("LoginController")) {
            return true;
        }

        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION".equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    log.info("쿠키 값: {}", cookieValue);
                }
            }
        }
        if(cookieValue == null){
            throw new RuntimeException("비로그인");
        }

        return true;
    }
}

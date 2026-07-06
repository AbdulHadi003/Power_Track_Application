package com.powertrack.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Ensure session is created for authenticated requests
        if (isAuthenticationRequest(request)) {
            HttpSession session = request.getSession(true);  // Create session if doesn't exist
            log.info("Session created/accessed: {}", session.getId());
        }
        return true;
    }

    private boolean isAuthenticationRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("/auth/register") || uri.contains("/auth/login");
    }
}
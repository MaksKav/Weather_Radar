package com.maxkavun.interceptor;

import com.maxkavun.service.AuthorizationService;
import com.maxkavun.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class SessionInterceptor implements HandlerInterceptor {

    private final AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var sessionId = CookieUtils.getSessionIdFromCookie(request);
        var requestUri = request.getRequestURI();

        if (sessionId != null && authorizationService.isSessionValid(sessionId) &&
            (Objects.equals(requestUri,"/") || Objects.equals(requestUri,"/registration"))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return false;
        }

        if (sessionId != null && authorizationService.isSessionValid(sessionId)) {
            return true;
        } else {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }

}

package com.maxkavun.interceptor;

import com.maxkavun.service.LoginService;
import com.maxkavun.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public SessionInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = CookieUtil.getSessionIdFromCookie(request);
        String requestUri = request.getRequestURI();

        if (sessionId != null && loginService.isSessionValid(sessionId) &&
            (requestUri.equals("/") || requestUri.equals("/registration"))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return false;
        }

        if (sessionId != null && loginService.isSessionValid(sessionId)) {
            return true;
        } else {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }

}

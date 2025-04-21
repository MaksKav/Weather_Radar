package com.maxkavun.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

public class CookieUtil {

    @Getter
    private static final String DEFAULT_SESSION_COOKIE_NAME = "CUSTOM_SESSION_ID";

    private CookieUtil() {}

    public static String getSessionIdFromCookie(HttpServletRequest request , String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String getSessionIdFromCookie(HttpServletRequest request) {
        return getSessionIdFromCookie(request , DEFAULT_SESSION_COOKIE_NAME);
    }
}

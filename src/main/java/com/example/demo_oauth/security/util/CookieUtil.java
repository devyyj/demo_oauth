package com.example.demo_oauth.security.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public Cookie createCookie(String name, String value) {

        Cookie jwtCookie = new Cookie(name, value);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600); // 토큰 만료 시간 (초)

        return jwtCookie;
    }

    public String getCookie(Cookie[] cookies, String name) {
        // 쿠키 배열 가져오기
        if (cookies != null) {
            // 쿠키에서 JWT 토큰 찾기
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // JWT 토큰이 쿠키에 없을 경우 null 반환
    }
}

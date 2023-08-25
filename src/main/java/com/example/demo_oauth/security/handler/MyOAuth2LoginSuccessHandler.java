package com.example.demo_oauth.security.handler;

import com.example.demo_oauth.security.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MyOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String username = oauth2User.getName();

        // JWT 토큰 생성
        String jwt = jwtProvider.generateToken(username);

        // JWT 토큰을 클라이언트에게 반환 (예: 응답 헤더에 추가)
        response.addHeader("Authorization", "Bearer " + jwt);

        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600); // 토큰 만료 시간 (초)
        response.addCookie(jwtCookie);

        // 로그인 성공 후 리다이렉트 또는 추가 작업을 수행할 수 있습니다.
//        clearAuthenticationAttributes(request);
//        super.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
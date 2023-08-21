package com.example.demo_oauth.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class MyOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // OAuth2 로그인 실패 후의 동작을 정의
        // 여기에서는 실패 시 리다이렉트 또는 추가 작업을 수행할 수 있습니다.

        // 예: 실패 시 로그인 페이지로 리다이렉트
        response.sendRedirect("/login?error=true");
    }
}
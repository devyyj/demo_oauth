package com.example.demo_oauth.security.handler;

import com.example.demo_oauth.security.util.CookieUtil;
import com.example.demo_oauth.security.util.JwtUtil;
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

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String username = oauth2User.getName();

        // JWT 토큰 생성
        String jwt = jwtUtil.generateToken(username);

        // JWT 토큰을 클라이언트에게 반환 (예: 응답 헤더에 추가)
        response.addHeader("Authorization", "Bearer " + jwt);

        // todo 프론트 개발 되면 해당 코드 제거, JWT는 헤더로 전송함
        Cookie jwtCookie = cookieUtil.createCookie("jwt", jwt);
        response.addCookie(jwtCookie);

        // 로그인 성공 후 리다이렉트 또는 추가 작업을 수행할 수 있습니다.
        clearAuthenticationAttributes(request);
        String prevUrl = cookieUtil.getCookie(request.getCookies(), "prevUrl");
        super.setDefaultTargetUrl(prevUrl == null ? "/" : prevUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
package com.example.demo_oauth.security.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo_oauth.security.entity.MyUser;
import com.example.demo_oauth.security.repository.MyUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class MyOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MyUserRepository userRepository;
    private final String jwtSecret = "시끄릿";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String username = oauth2User.getName();

        // 회원 가입
        if(userRepository.findBySocialId(username).isEmpty()) {
            userRepository.save(MyUser.builder().nickName(username).socialId(username).build());
        }

        // JWT 클레임 설정
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("sub", oauth2User.getAttribute("sub")); // 사용자 ID
//        claims.put("name", oauth2User.getAttribute("name")); // 사용자 이름

        // JWT 토큰 생성
        String jwtToken = JWT.create()
                .withSubject(oauth2User.getName()) // 일반적으로는 사용자 이름을 주체로 설정
//                .withClaim("userDetails", sub) // 기타 사용자 정보 추가
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1시간 유효한 토큰
                .sign(Algorithm.HMAC256(jwtSecret));

        // JWT 토큰을 클라이언트에게 반환 (예: 응답 헤더에 추가)
        response.addHeader("Authorization", "Bearer " + jwtToken);

        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(3600); // 토큰 만료 시간 (초)
        response.addCookie(jwtCookie);

        // 로그인 성공 후 리다이렉트 또는 추가 작업을 수행할 수 있습니다.
        clearAuthenticationAttributes(request);
        super.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
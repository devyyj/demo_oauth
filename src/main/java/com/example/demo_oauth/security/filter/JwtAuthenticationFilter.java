package com.example.demo_oauth.security.filter;

import com.example.demo_oauth.security.util.CookieUtil;
import com.example.demo_oauth.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // todo 쿠키가 아니라 헤더에서 가져와야 함, 현재는 프론트 없이 테스트 중
        String jwt = cookieUtil.getCookie(request.getCookies(), "jwt");
        String username = jwtUtil.verifyToken(jwt);

        if (username != null) {
            // 권한 설정 꼭 필요! 하지 않으면 무한 인증 요청
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(
                    username
                    , ""
                    , List.of(new SimpleGrantedAuthority("USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            String prevUrl = request.getRequestURI();
            if (!prevUrl.equals("/favicon.ico")) {
                Cookie prevUrlCookie = cookieUtil.createCookie("prevUrl", prevUrl);
                response.addCookie(prevUrlCookie);
            }
        }

        filterChain.doFilter(request, response);
    }
}

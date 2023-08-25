package com.example.demo_oauth.security.filter;

import com.example.demo_oauth.security.util.JwtProvider;
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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = getTokenFromCookie(request.getCookies());

        log.info("jwt : " + jwt);

        String username = jwtProvider.verifyToken(jwt);

        log.info("username : " + username);

        if (username != null) {
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(username, "", List.of(new SimpleGrantedAuthority("SARAM")));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            Cookie prevUrlCookie = new Cookie("prevUrl", request.getRequestURI());
            prevUrlCookie.setPath("/");
            response.addCookie(prevUrlCookie);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(Cookie[] cookies) {
        // 쿠키 배열 가져오기
        if (cookies != null) {
            // 쿠키에서 JWT 토큰 찾기
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // JWT 토큰이 쿠키에 없을 경우 null 반환
    }
}

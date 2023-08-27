package com.example.demo_oauth.security;

import com.example.demo_oauth.security.filter.JwtAuthenticationFilter;
import com.example.demo_oauth.security.handler.MyOAuth2LoginFailureHandler;
import com.example.demo_oauth.security.handler.MyOAuth2LoginSuccessHandler;
import com.example.demo_oauth.security.service.MyDefaultOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final MyOAuth2LoginSuccessHandler successHandler;
    private final MyOAuth2LoginFailureHandler failureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MyDefaultOAuth2UserService myDefaultOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // URL 전체 허용이 맨 처음 나오면 에러남
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(c -> c.successHandler(successHandler)
                        .failureHandler(failureHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

package com.example.demo_oauth.security;

import com.example.demo_oauth.security.filter.JwtAuthenticationFilter;
import com.example.demo_oauth.security.handler.MyExceptionHandler;
import com.example.demo_oauth.security.handler.MyOAuth2LoginFailureHandler;
import com.example.demo_oauth.security.handler.MyOAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final MyOAuth2LoginSuccessHandler successHandler;
    private final MyOAuth2LoginFailureHandler failureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MyExceptionHandler myExceptionHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(c -> c.authenticationEntryPoint(myExceptionHandler))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(c -> c.successHandler(successHandler)
                        .failureHandler(failureHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

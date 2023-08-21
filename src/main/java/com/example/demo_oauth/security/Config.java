package com.example.demo_oauth.security;

import com.example.demo_oauth.security.handler.MyOAuth2LoginFailureHandler;
import com.example.demo_oauth.security.handler.MyOAuth2LoginSuccessHandler;
import com.example.demo_oauth.security.repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final MyUserRepository userRepository;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(withDefaults())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(c -> c.successHandler(new MyOAuth2LoginSuccessHandler(userRepository)).failureHandler(new MyOAuth2LoginFailureHandler()));
        return http.build();
    }
}

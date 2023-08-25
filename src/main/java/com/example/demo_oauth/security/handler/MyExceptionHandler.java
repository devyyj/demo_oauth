package com.example.demo_oauth.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class MyExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("=========================================");
        log.info(response.getStatus());
        log.info(authException.getMessage());
        log.info("=========================================");

        // 여기서 원하는 에러 코드를 설정할 수 있습니다. 예를 들어, 401 Unauthorized
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}
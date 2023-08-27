package com.example.demo_oauth.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-secret-key"; // 실제로는 보안에 강한 랜덤한 키를 사용해야 합니다.

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1시간 유효한 토큰

        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String verifyToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);

            // 토큰이 유효한 경우, 주체(subject)를 반환합니다.
            return jwt.getSubject();
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우, 예외 처리를 수행합니다.
            // 유효하지 않은 토큰 또는 만료된 토큰일 수 있습니다.
            return null;
        }
    }
}

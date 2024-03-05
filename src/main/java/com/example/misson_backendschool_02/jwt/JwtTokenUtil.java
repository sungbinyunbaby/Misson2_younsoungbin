package com.example.misson_backendschool_02.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;

// jwt 설정
@Slf4j
//@RequiredArgsConstructor
@Component
public class JwtTokenUtil {
    // 1. 암호화
    // 2. jwt에 정보 담기 jwt 생성
    // 3. 발급한 jwt 토큰이 정상인지 확인
    // 4. jwt 토큰이 정상이면 발급.


    private final Key key;

    private final JwtParser parser;

    // 암호화
    public JwtTokenUtil(
            @Value("skdmlrldjrdjelsrkdpejdrmaksltnadjdlTEk")
            String jwtSecret
    ){
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }

    // jwt에 정보 담기, jwt 생성
    public String generateToken(
            UserDetails userDetails,
            String role
    ) {
        log.info("*** 토큰을 생성하겠습니다. ***");
        Instant now = Instant.now();

     Claims jwtClaims = Jwts.claims()
             .setSubject(userDetails.getUsername())
             .setIssuedAt(Date.from(now))
             .setExpiration(Date.from(now.plusSeconds(20000L)));
     jwtClaims.put("role", role);

     return Jwts.builder()
             .setClaims(jwtClaims)
             .signWith(key)
             .compact();
    }


    // 발급한 jwt 토큰이 정상인지 확인
    public boolean validate(String token) {
        try {
            parser.parseClaimsJws(token);
            log.info("*** 토큰확인: 정상적인 토큰입니다.");
            return true;
        } catch (Exception e) {
            log.info("*** 토큰확인: 정상적이지 않은 토큰입니다.");
        }
        return false;
    }

    // jwt 토큰이 정상이면 발급.
    public Claims parseClaims(String token) {
        return parser
                .parseClaimsJws(token)
                .getBody();
    }

}

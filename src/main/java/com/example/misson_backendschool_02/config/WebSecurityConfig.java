package com.example.misson_backendschool_02.config;

import com.example.misson_backendschool_02.jwt.JwtTokenFilter;
import com.example.misson_backendschool_02.jwt.JwtTokenUtil;
import com.example.misson_backendschool_02.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login")
                        .permitAll()

                        // 모든 게시글을 다 적을 수 없으니 collection으로 만들어 줄 수 있을까?
                        // => ㄴㄴ 그냥 모든 게시글의 url을 /comments로 하면 된다.
                        //
                        .requestMatchers("/comments/***")
//                        .hasRole("MEMBER")
                        .hasAnyRole("MEMBER", "BUSINESS")

                        // 회원가입 후 권한이 inactive로 제대로 설정이 되었다면 접근가능.
                        .requestMatchers("/authorization/inactive")
                        .hasRole("INACTIVE")

                        .requestMatchers("/create/inactive")
                        .permitAll()

                        // 권한 inactive를 member로 변경하기 위함.
                        .requestMatchers("/changeToRoleMember")
                        .hasRole("INACTIVE")

                        // 등록된 물품 정보는 비활성 사용자를 제외 누구든지 열람이 가능하다.
                        .requestMatchers("/register/***")
                        .hasAnyRole("MEMBER", "BUSINESS", "ADMIN")

                        // 물품을 등록한 사용자와 비활성 사용자 제외, 등록된 물품에 대하여 구매 제안을 등록할 수 있다.
                        .requestMatchers("/proposal/***")
                        .hasAnyRole("MEMBER", "BUSINESS", "ADMIN")

                        // 쇼핑몰, 의문점: 개인과 관리자만 접속할 수 있어야 하는데,,, 흠,,, 우선 일단 진행!
                                .requestMatchers("/shop/***")
                                .hasAnyRole("MEMBER", "BUSINESS", "ADMIN")


                )

                //세션 비활성화
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenUtil, userDetailsService)
                        , LoginFilter.class)

                .addFilterAt(
                        new LoginFilter(
                                authenticationManager(authenticationConfiguration),
                                jwtTokenUtil
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
        ;
        return httpSecurity.build();
    }
}

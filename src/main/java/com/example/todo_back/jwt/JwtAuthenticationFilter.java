package com.example.todo_back.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtAuthenticationProvider;

    public JwtAuthenticationFilter(JwtTokenProvider provider) {
        jwtAuthenticationProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //토큰을 if문에서 인증하고, 토큰에서 아이디 찾고 customUserDetailsService 에서 loadUserByUsername 을 해서 받아온 뒤 유저 권한을 부여하여 authentication 을 생성
        //생성한 authentication 을 SecurityContextHolder 에 저장
        String token = jwtAuthenticationProvider.resolveToken(request);
        log.info("토큰은 {}", token);
        if (token != null && jwtAuthenticationProvider.validateToken(token)) {
            log.info("1번자리@@@@@@@@@@@@@@@@@@@@@");
            Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
            log.info("2번자리@@@@@@@@@@@@@@@@@@@@@");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("3번자리@@@@@@@@@@@@@@@@@@@@@");
        }

        filterChain.doFilter(request, response);

    }

}

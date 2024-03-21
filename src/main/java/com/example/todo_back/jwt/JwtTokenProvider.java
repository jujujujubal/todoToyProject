package com.example.todo_back.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Data
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 168 시간(7일)
    private final CustomUserDetailsService customUserDetailsService;
    private static final long TOKEN_VALID_TIME = 1440 * 60 * 7 * 1000L;
    public static final String HEADER_ACCESS_TOKEN = "Authorization";
    public static final String HEADER_REFRESH_TOKEN = "X-REFRESH-TOKEN";

    // secretKey 를 Base64로 인코딩합니다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String personalId, String role) {
        Claims claims = Jwts.claims().setSubject(personalId);
        claims.put("role", role); // 정보는 key/value 쌍으로 저장됩니다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // 토큰 유효 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getPersonalId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getPersonalId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 의 Header 에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_ACCESS_TOKEN);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            //만료된 토큰은 전부 catch 되기에 !claims.getBody().getExpiration().before(new Date()) 는 항상 true 이다
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

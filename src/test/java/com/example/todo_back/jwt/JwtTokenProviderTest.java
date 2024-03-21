package com.example.todo_back.jwt;

import com.example.todo_back.data.constant.Role;
import com.example.todo_back.data.entity.MemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static com.example.todo_back.data.constant.ColorList.YELLOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {CustomUserDetailsService.class, JwtTokenProvider.class})
@ExtendWith(SpringExtension.class)
class JwtTokenProviderTest {

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("제대로 Base64로 인코딩되는지 테스트")
    void init_base64() {
        String secretKey = "fakeSecretKey";
        jwtTokenProvider.setSecretKey(secretKey);
        jwtTokenProvider.init();

        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        assertEquals(encodedSecretKey, jwtTokenProvider.getSecretKey());
    }

    @Test
    void createToken() {
        String testPersonalId = "testPersonalId";
        String role = "ROLE_USER";

        String accessToken = jwtTokenProvider.createToken(testPersonalId, role);

        assertNotNull(accessToken);

        Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(accessToken).getBody();
        assertEquals(testPersonalId, claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void getAuthentication() {
        String testPersonalId = "testPersonalId";
        String role = "ROLE_USER";
        String testUserToken = jwtTokenProvider.createToken(testPersonalId, role);

        UserDetails userDetails = (UserDetails) MemberEntity.builder().personalId(testPersonalId).password("test123456").nickname("dummy").role(Role.ROLE_USER).color(YELLOW).build();

        Mockito.when(customUserDetailsService.loadUserByUsername(testPersonalId)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(testUserToken);

        assertEquals(new UsernamePasswordAuthenticationToken(userDetails, "test123456", userDetails.getAuthorities()), authentication);

        verify(customUserDetailsService).loadUserByUsername(testPersonalId);
    }

    @Test
    void getPersonalId() {
        String testPersonalId = "testPersonalId";
        String testUserToken = jwtTokenProvider.createToken(testPersonalId, "ROLE_USER");

        String returnPersonalId = jwtTokenProvider.getPersonalId(testUserToken);

        assertEquals(testPersonalId, returnPersonalId);
    }

    @Test
    @DisplayName("토큰이 유효한 경우")
    void resolveToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String testToken = "Bearer validToken";
        request.addHeader("Authorization", testToken);

        String resolvedToken = jwtTokenProvider.resolveToken(request);

        assertEquals("validToken", resolvedToken);
    }

    @Test
    @DisplayName("토큰이 null인 경우")
    void resolveToken_nothing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String returnString = jwtTokenProvider.resolveToken(request);
        assertNull(returnString);
    }

    @Test
    @DisplayName("토큰이 Bearer로 시작하지 않는 경우")
    void resolveToken_invalid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String testToken = "invalidToken";
        request.addHeader("Authorization", testToken);

        String returnString = jwtTokenProvider.resolveToken(request);
        assertNull(returnString);
    }

    @Test
    @DisplayName("valid Token")
    void validateToken() {
        String testUserToken = jwtTokenProvider.createToken("testPersonalId", "ROLE_USER");
        boolean isValid = jwtTokenProvider.validateToken(testUserToken);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("expired Token")
    void validateToken_expired() {
        Claims claims = Jwts.claims().setSubject("testPersonalId");
        claims.put("role", "ROLE_USER");
        Date now = new Date();
        String testUserToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime())) // 토큰이 만료되도록 설정
                .signWith(SignatureAlgorithm.HS256, jwtTokenProvider.getSecretKey())
                .compact();

        boolean isValid = jwtTokenProvider.validateToken(testUserToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("invalid Token")
    void validateToken_invalid() {
        String testUserToken = "invalidToken";
        boolean isValid = jwtTokenProvider.validateToken(testUserToken);
        assertFalse(isValid);
    }
}

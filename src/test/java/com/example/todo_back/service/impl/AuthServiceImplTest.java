package com.example.todo_back.service.impl;

import com.example.todo_back.data.constant.Role;
import com.example.todo_back.data.dto.LoginDto;
import com.example.todo_back.data.dto.SignUpDto;
import com.example.todo_back.data.entity.MemberEntity;
import com.example.todo_back.data.repository.MemberRepository;
import com.example.todo_back.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.example.todo_back.data.constant.ColorList.YELLOW;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {AuthServiceImpl.class, MemberRepository.class, PasswordEncoder.class, JwtTokenProvider.class})
@ExtendWith(SpringExtension.class)
//@Import({AuthServiceImpl.class, MemberRepository.class, PasswordEncoder.class, JwtTokenProvider.class})
class AuthServiceImplTest {

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthServiceImpl authService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp() {
        SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(YELLOW).build();
        //처음 if문에서 false가 나오도록
        Mockito.when(memberRepository.findByPersonalId(signUpDto.getPersonalId())).thenReturn(Optional.empty());
        Mockito.when(memberRepository.save(signUpDto.toEntity())).thenReturn(signUpDto.toEntity());

        Boolean result = authService.signUp(signUpDto);

        Assertions.assertTrue(result);

        verify(memberRepository).save(signUpDto.toEntity());
//        verify(memberRepository, Mockito.times(1)).save(Mockito.any(MemberEntity.class));
        verify(passwordEncoder).encode(signUpDto.toEntity().getPassword());

    }

    @Test
    @DisplayName("회원가입 실패 - 중복 아이디")
    void signUp_duplication() {
        SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(YELLOW).build();

        //처음 if문에서 true가 나오도록
        Mockito.when(memberRepository.findByPersonalId(signUpDto.getPersonalId())).thenReturn(Optional.of(new MemberEntity()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpDto));
    }
    @Test
    @DisplayName("로그인 성공")
    void login() {
        LoginDto loginDto = LoginDto.builder().personalId("test123456").password("test123456").build();
        MemberEntity memberEntity = MemberEntity.builder().personalId("test123456").password(passwordEncoder.encode("test123456")).nickname("dummy").role(Role.ROLE_USER).color(YELLOW).build();
        String dummy_token = "dummy_token";

        Mockito.when(memberRepository.findByPersonalId(loginDto.getPersonalId())).thenReturn(Optional.of(memberEntity));

        //if 문에서 true가 나오도록
        Mockito.when(passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword())).thenReturn(true);

        Mockito.when(jwtTokenProvider.createToken(memberEntity.getPersonalId(), memberEntity.getRole().name())).thenReturn(dummy_token);

        String result = authService.login(loginDto);

        Assertions.assertEquals(result, dummy_token);

        verify(memberRepository).findByPersonalId(loginDto.getPersonalId());
        verify(passwordEncoder).matches(loginDto.getPassword(), memberEntity.getPassword());
        verify(jwtTokenProvider).createToken(memberEntity.getPersonalId(), memberEntity.getRole().name());
    }

    @Test
    @DisplayName("로그인 실패 - 일치하는 아이디 없음")
    void login_nothing() {
        LoginDto loginDto = LoginDto.builder().personalId("test123456").password("test123456").build();

        Mockito.when(memberRepository.findByPersonalId(loginDto.getPersonalId())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> authService.login(loginDto));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_wrong() {
        LoginDto loginDto = LoginDto.builder().personalId("test123456").password("test123456").build();
        MemberEntity memberEntity = MemberEntity.builder().personalId("test123456").password(passwordEncoder.encode("test1234")).nickname("dummy").role(Role.ROLE_USER).color(YELLOW).build();
        Mockito.when(memberRepository.findByPersonalId(loginDto.getPersonalId())).thenReturn(Optional.of(memberEntity));

        //if 문에서 true가 나오도록
        Mockito.when(passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword())).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> authService.login(loginDto));
    }
}

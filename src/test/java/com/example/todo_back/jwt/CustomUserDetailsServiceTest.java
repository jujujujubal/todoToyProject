package com.example.todo_back.jwt;

import com.example.todo_back.data.constant.Role;
import com.example.todo_back.data.entity.MemberEntity;
import com.example.todo_back.data.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.example.todo_back.data.constant.ColorList.YELLOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MemberRepository.class, CustomUserDetailsService.class})
@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("유저 찾기 - 성공")
    void loadUserByUsername() {
        String input = "test123456";
        MemberEntity memberEntity = MemberEntity.builder().personalId(input).password("test123456").nickname("dummy").role(Role.ROLE_USER).color(YELLOW).build();
        Mockito.when(memberRepository.findByPersonalId(input)).thenReturn(Optional.of(memberEntity));

        MemberEntity result = (MemberEntity) customUserDetailsService.loadUserByUsername(input);

        Assertions.assertEquals(memberEntity, result);

        verify(memberRepository).findByPersonalId(input);
    }

    @Test
    @DisplayName("유저 찾기 - 실패")
    void loadUserByUsername_nothing() {
        String input = "test123456";
        Mockito.when(memberRepository.findByPersonalId(input)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> customUserDetailsService.loadUserByUsername(input));
    }
}

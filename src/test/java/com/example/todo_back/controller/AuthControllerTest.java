package com.example.todo_back.controller;

import com.example.todo_back.config.SecurityConfig;
import com.example.todo_back.data.dto.LoginDto;
import com.example.todo_back.data.dto.SignUpDto;
import com.example.todo_back.jwt.CustomUserDetailsService;
import com.example.todo_back.jwt.JwtTokenProvider;
import com.example.todo_back.mock.WithCustomMockUser;
import com.example.todo_back.service.impl.AuthServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.todo_back.data.constant.ColorList.yellow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
//@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Import({JwtTokenProvider.class})
class AuthControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthServiceImpl authService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    //@WithCustomMockUser
    @DisplayName("회원가입 테스트")
    void signUp() {
        // 회원가입 시 SignUpDto 로 personalId, password, nickname, color 을 받음
        // 이미 존재하는 아이디를 입력한 경우 (500)
        // 아이디 형식이 올바르지 않은 경우 (400 에러)
        // 패스워드 형식이 올바르지 않은 경우 (400 에러)
        // 닉네임 형식이 올바르지 않은 경우 (아직 닉네임 형식까지는 생각 안 함)
        // 올바른 경우
        SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(yellow).build();

        given(authService.signUp(signUpDto)).willReturn(true);

        Gson gson = new Gson();
        String content = gson.toJson(signUpDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/signUp").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").exists())
                    .andExpect(jsonPath("$.error_code").exists())
                    .andExpect(jsonPath("$.item").exists())
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);

        }

        verify(authService).signUp(signUpDto);
        //given 어떠한 데이터가 주어질때
        //when 어떠한 기능을 실행하면
        //then 어떠한 결과를 기대한다.
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // 로그인 시 LoginDto 로 personalId, password 를 받음
        // 아이디 형식이 올바르지 않은 경우 (400 에러)
        // 패스워드 형식이 올바르지 않은 경우 (400 에러)
        // 회원정보에 일치하는 아이디가 없는 경우 (500)
        // 아이디는 존재하지만 패스워드가 틀린 경우 (500)
        // 올바른 경우
        LoginDto loginDto = LoginDto.builder().personalId("test123456").password("test123456").build();

        given(authService.login(loginDto)).willReturn("token");

        Gson gson = new Gson();
        String content = gson.toJson(loginDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/login").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").exists())
                    .andExpect(jsonPath("$.error_code").exists())
                    .andExpect(jsonPath("$.item").exists())
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);

        }

        verify(authService).login(loginDto);
    }
}

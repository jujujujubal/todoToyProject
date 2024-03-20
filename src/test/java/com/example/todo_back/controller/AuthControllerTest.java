package com.example.todo_back.controller;

import com.example.todo_back.config.SecurityConfig;
import com.example.todo_back.data.dto.LoginDto;
import com.example.todo_back.data.dto.SignUpDto;
import com.example.todo_back.jwt.CustomUserDetailsService;
import com.example.todo_back.jwt.JwtTokenProvider;
import com.example.todo_back.service.impl.AuthServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.todo_back.data.constant.ColorList.YELLOW;
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
@Import({JwtTokenProvider.class, SecurityConfig.class})
@Slf4j
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
    @DisplayName("회원가입 성공")
    void signUp() {
        // 회원가입 시 SignUpDto 로 personalId, password, nickname, color 을 받음
        // 이미 존재하는 아이디를 입력한 경우 (409 에러)
        // 아이디 형식이 올바르지 않은 경우 (400 에러)
        // 패스워드 형식이 올바르지 않은 경우 (400 에러)
        // 닉네임 형식이 올바르지 않은 경우 (아직 닉네임 형식까지는 생각 안 함)
        // 올바른 경우
        SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(YELLOW).build();

        given(authService.signUp(signUpDto)).willReturn(true);

        Gson gson = new Gson();
        String content = gson.toJson(signUpDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/signUp").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.error_code").value(0))
                    .andExpect(jsonPath("$.item").value(true))
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
    //@WithCustomMockUser
    @DisplayName("회원가입 실패 - 이미 존재하는 아이디")
    void signUp_duplication() {
        // 이미 존재하는 아이디를 입력한 경우
        SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(YELLOW).build();

        given(authService.signUp(signUpDto)).willThrow(new IllegalArgumentException("존재하는 아이디입니다."));

        Gson gson = new Gson();
        String content = gson.toJson(signUpDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/signUp")
                                    .content(content)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error_code").value(409))
                    .andExpect(jsonPath("$.item").value("존재하는 아이디입니다."))
                    .andDo(print());

        } catch (Exception e) {
            logger.error("Exception occurred during mockMvc.perform()", e);
        }
        verify(authService).signUp(signUpDto);
    }

    @Test
    @DisplayName("로그인 성공")
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
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.error_code").value(0))
                    .andExpect(jsonPath("$.item").value("token"))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(authService).login(loginDto);
    }

    @Test
    @DisplayName("로그인 실패 - 입력 형식 불일치")
    void login_invalid() {
        LoginDto loginDto = LoginDto.builder().personalId("test").password("test123456").build();

        Gson gson = new Gson();
        String content = gson.toJson(loginDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/login").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error_code").value(400))
                    .andExpect(jsonPath("$.item").value("입력 형식이 알맞지 않습니다."))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 아이디")
    void login_nothing() {
        LoginDto loginDto = LoginDto.builder().personalId("test123456").password("test123456").build();

        given(authService.login(loginDto)).willThrow(new IllegalArgumentException("일치하는 회원이 없습니다."));

        Gson gson = new Gson();
        String content = gson.toJson(loginDto);

        try {
            mockMvc.perform(
                            post("/api/v1/auth/login").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error_code").value(404))
                    .andExpect(jsonPath("$.item").value("일치하는 회원이 없습니다."))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(authService).login(loginDto);
    }
}

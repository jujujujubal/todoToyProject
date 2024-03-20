package com.example.todo_back.controller;

import com.example.todo_back.config.SecurityConfig;
import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.jwt.CustomUserDetailsService;
import com.example.todo_back.jwt.JwtTokenProvider;
import com.example.todo_back.mock.WithCustomMockUser;
import com.example.todo_back.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;

import java.util.List;

import static com.example.todo_back.data.constant.ColorList.YELLOW;
import static com.example.todo_back.data.constant.ToDoEntityStatus.UNCOMPLETED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ToDoController.class)
//@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Import({JwtTokenProvider.class, SecurityConfig.class})
class ToDoControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ToDoServiceImpl toDoService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    @DisplayName("ToDo 전부 받아오기 테스트")
    void getToDo() {
        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("test_id").content("test_content").isComplete(UNCOMPLETED).build();
        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId("string").userNickname("admin").title("test_title").content(List.of(contentDto)).color(YELLOW).build();

        given(toDoService.getToDo()).willReturn(List.of(postDto));

        try {
            mockMvc.perform(
                            get("/api/v1/todo/load").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(toDoService).getToDo();
        //given 어떠한 데이터가 주어질때
        //when 어떠한 기능을 실행하면
        //then 어떠한 결과를 기대한다.
    }

    @Test
    @WithCustomMockUser
    @DisplayName("ToDo 업로드 테스트")
    void createToDo() {
        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummyTitle").content(List.of(toDoRequestContentDto)).build();

        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("test_id").content("test_content").isComplete(UNCOMPLETED).build();
        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId("string").userNickname("Test_nickname").title("test_title").content(List.of(contentDto)).color(YELLOW).build();

        //WithCustomMockUser 에서 지정한 유저 아이디
        String dummy_id = "TestPersonalId";

        given(toDoService.saveToDo(toDoRequestPostDto, dummy_id)).willReturn(postDto);

        Gson gson = new Gson();

        String content = gson.toJson(toDoRequestPostDto);

        try {
            mockMvc.perform(
                            post("/api/v1/todo/create").content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(toDoService).saveToDo(toDoRequestPostDto, dummy_id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void updatePost() {//일치하는 postId가 없어서 에러가 발생하는 경우도 다뤄야함
        String postId = "qwerty";
        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummyTitle").content(List.of(toDoRequestContentDto)).build();

        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("test_id").content("test_content").isComplete(UNCOMPLETED).build();
        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId("string").userNickname("Test_nickname").title("test_title").content(List.of(contentDto)).color(YELLOW).build();

        given(toDoService.updateToDo(toDoRequestPostDto, postId)).willReturn(postDto);

        Gson gson = new Gson();
        String content = gson.toJson(toDoRequestPostDto);

        try {
            mockMvc.perform(
                            put("/api/v1/todo/update/" + postId).content(content).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(toDoService).updateToDo(toDoRequestPostDto, postId);

    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void changeComplete() {//일치하는 contentId가 없어서 에러가 발생하는 경우도 다뤄야함
        String contentId = "qwerty";

        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id(contentId).content("string").isComplete(UNCOMPLETED).build();

        given(toDoService.changeComplete(contentId)).willReturn(contentDto);

        try {
            mockMvc.perform(
                            put("/api/v1/todo/update/content/" + contentId).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(toDoService).changeComplete(contentId);
    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void deleteToDo() {
        String postId = "qwerty";
        String returnString = "정상적으로 삭제되었습니다.";

        doNothing().when(toDoService).deleteToDo(postId);

        try {
            mockMvc.perform(
                            delete("/api/v1/todo/delete/" + postId).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(returnString))
                    .andDo(print());
        } catch (Exception e) {
            logger.error("An error occurred during mockMvc.perform()", e);
        }

        verify(toDoService).deleteToDo(postId);
    }
}

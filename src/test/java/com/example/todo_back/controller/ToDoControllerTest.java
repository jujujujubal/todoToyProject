package com.example.todo_back.controller;

import com.example.todo_back.config.GsonUtils;
import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.jwt.CustomUserDetailsService;
import com.example.todo_back.jwt.JwtTokenProvider;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.example.todo_back.data.constant.ColorList.yellow;
import static com.example.todo_back.data.constant.ToDoEntityStatus.uncompleted;
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
@Import({JwtTokenProvider.class})
class ToDoControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ToDoServiceImpl toDoService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

//    @Test
//    @WithMockUser(username = "admin", roles = "User")
//    @DisplayName("ToDo 전부 받아오기 테스트")
//    void getToDo() {
//        //SignUpDto signUpDto = SignUpDto.builder().personalId("test123456").password("test123456").nickname("테스트").color(yellow).build();
//        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("string").content("string").isComplete(uncompleted).build();
//        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId("string").userId("string").title("string").content(List.of(contentDto)).color(yellow).build();
//
//        given(toDoService.getToDo()).willReturn(List.of(postDto));
//
//        try {
//            mockMvc.perform(
//                            get("/api/v1/todo/load").contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andDo(print());
//        } catch (Exception e) {
//            logger.error("An error occurred during mockMvc.perform()", e);
//        }
//
//        verify(toDoService).getToDo();
//        //given 어떠한 데이터가 주어질때
//        //when 어떠한 기능을 실행하면
//        //then 어떠한 결과를 기대한다.
//    }

//    @Test
//    @WithMockUser(username = "admin", roles = "User")
//    @DisplayName("ToDo 업로드 테스트")
//    void createToDo() {
//        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(uncompleted).indexNum("1").build();
//        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().userId("dummyUserId").title("dummyTitle").content(List.of(toDoRequestContentDto)).color(yellow).build();
//
//        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("string").content("string").isComplete(uncompleted).build();
//        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId("string").userId("string").title("string").content(List.of(contentDto)).createTime(LocalDateTime.now()).color(yellow).build();
//
//        given(toDoService.saveToDo(toDoRequestPostDto)).willReturn(postDto);
//
//        String content = GsonUtils.toJson(toDoRequestPostDto);
//
//        try {
//            mockMvc.perform(
//                            post("/api/v1/todo/create").content(content).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andDo(print());
//        } catch (Exception e) {
//            logger.error("An error occurred during mockMvc.perform()", e);
//        }
//
//        verify(toDoService).saveToDo(toDoRequestPostDto);
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "User")
//    void updatePost() {
//        String postId = "qwerty";
//        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(uncompleted).indexNum("1").build();
//        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().userId("dummyUserId").title("dummyTitle").content(List.of(toDoRequestContentDto)).color(yellow).build();
//
//        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id("string").content("string").isComplete(uncompleted).build();
//        ToDoResponsePostDto postDto = ToDoResponsePostDto.builder().postId(postId).userId("string").title("string").content(List.of(contentDto)).createTime(LocalDateTime.now()).color(yellow).build();
//
//        given(toDoService.updateToDo(toDoRequestPostDto, postId)).willReturn(postDto);
//
//        String content = GsonUtils.toJson(toDoRequestPostDto);
//
//        try {
//            mockMvc.perform(
//                            put("/api/v1/todo/update/" + postId).content(content).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andDo(print());
//        } catch (Exception e) {
//            logger.error("An error occurred during mockMvc.perform()", e);
//        }
//
//        verify(toDoService).updateToDo(toDoRequestPostDto, postId);
//
//    }

//    @Test
//    @WithMockUser(username = "admin", roles = "User")
//    void changeComplete() {
//        String contentId = "qwerty";
//
//        ToDoResponseContentDto contentDto = ToDoResponseContentDto.builder().id(contentId).content("string").isComplete(uncompleted).build();
//
//        given(toDoService.changeComplete(contentId)).willReturn(contentDto);
//
//
//        try {
//            mockMvc.perform(
//                            put("/api/v1/todo/update/content/" + contentId).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andDo(print());
//        } catch (Exception e) {
//            logger.error("An error occurred during mockMvc.perform()", e);
//        }
//
//        verify(toDoService).changeComplete(contentId);
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "User")
//    void deleteToDo() {
//        String postId = "qwerty";
//        String returnString = "정상적으로 삭제되었습니다.";
//
//        doNothing().when(toDoService).deleteToDo(postId);
//
//        try {
//            mockMvc.perform(
//                            delete("/api/v1/todo/delete/" + postId).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string("정상적으로 삭제되었습니다."))
//                    .andDo(print());
//        } catch (Exception e) {
//            logger.error("An error occurred during mockMvc.perform()", e);
//        }
//
//        verify(toDoService).deleteToDo(postId);
//    }
}

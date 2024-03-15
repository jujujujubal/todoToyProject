package com.example.todo_back.service.impl;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.PostEntity;
import com.example.todo_back.data.repository.ContentRepository;
import com.example.todo_back.data.repository.MemberRepository;
import com.example.todo_back.data.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.todo_back.data.constant.ColorList.yellow;
import static com.example.todo_back.data.constant.ToDoEntityStatus.uncompleted;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {ToDoServiceImpl.class, PostRepository.class, ContentRepository.class, MemberRepository.class})
@ExtendWith(SpringExtension.class)
class ToDoServiceImplTest {

    @MockBean
    PostRepository postRepository;

    @MockBean
    ContentRepository contentRepository;

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    ToDoServiceImpl toDoService;

//    @Test
//    @DisplayName("ToDo 전부 받아오기")
//    void getToDo() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        ContentEntity contentEntity = ContentEntity.builder().id("1").content("내용").isComplete(ToDoEntityStatus.uncompleted).indexNum("1").build();
//        PostEntity postEntity = PostEntity.builder().postId("1").userId("admin").title("dummy_title").createTime(localDateTime).color(ColorList.yellow).contents(List.of(contentEntity)).build();
//
//        Mockito.when(postRepository.findAll(Sort.by(Sort.Direction.ASC, "createTime"))).thenReturn(List.of(postEntity));
//
//        List<ToDoResponsePostDto> toDoResponsePostDtoList = toDoService.getToDo();
//
//        Assertions.assertEquals(toDoResponsePostDtoList, List.of(postEntity.toDto()));
//
//        verify(postRepository).findAll(Sort.by(Sort.Direction.ASC, "createTime"));
//    }

//    @Test
//    void saveToDo() {
//        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummy_content").isComplete(uncompleted).indexNum("1").build();
//        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().userId("dummy_userId").title("dummy_title").content(List.of(toDoRequestContentDto)).color(yellow).build();
//
//        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("dummy_userId").title("dummy_title").color(yellow).build();
//        ContentEntity contentEntity = ContentEntity.builder().id("dummy_Id").post(postEntity).content("dummy_content").isComplete(uncompleted).indexNum("1").build();
//
//        //randomUUID 로 인해 값이 항상 달라지기에 Mockito.any(PostEntity.class)로 처리함
//        Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);
//        Mockito.when(contentRepository.save(Mockito.any(ContentEntity.class))).thenReturn(contentEntity);
//
//        ToDoResponsePostDto toDoResponsePostDto = toDoService.saveToDo(toDoRequestPostDto);
//
//        //안 넣었던 거 넣어주기
//        postEntity.setContents(List.of(contentEntity));
//
//        Assertions.assertEquals(toDoResponsePostDto, postEntity.toDto());
//
//        verify(postRepository).save(Mockito.any(PostEntity.class));
//        verify(contentRepository).save(Mockito.any(ContentEntity.class));
//    }

//    @Test
//    @DisplayName("ToDo 업데이트 성공")
//    void updateToDo() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        String dummy_postId = "dummy_postId";
//
//        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummy_content").isComplete(uncompleted).indexNum("1").build();
//        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().userId("dummy_userId").title("dummy_title").content(List.of(toDoRequestContentDto)).color(yellow).build();
//
//        ContentEntity contentEntity = ContentEntity.builder().id("1").content("dummy_content").isComplete(ToDoEntityStatus.uncompleted).indexNum("1").build();
//        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("dummy_userId").title("dummy_title").createTime(localDateTime).color(ColorList.yellow).contents(List.of(contentEntity)).build();
//
//        Mockito.when(postRepository.findById(dummy_postId)).thenReturn(Optional.of(postEntity));
//        //deleteToDo 와 saveToDo 를 위한 when들
//        Mockito.when(postRepository.findAll(Sort.by(Sort.Direction.ASC, "createTime"))).thenReturn(List.of(postEntity));
//        Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);
//        Mockito.when(contentRepository.save(Mockito.any(ContentEntity.class))).thenReturn(contentEntity);
//
//        ToDoResponsePostDto toDoResponsePostDto = toDoService.updateToDo(toDoRequestPostDto, dummy_postId);
//        //System.out.println("값1 " + toDoResponsePostDto);
//
//        Assertions.assertEquals(toDoResponsePostDto, postEntity.toDto());
//
//        verify(postRepository).findById(dummy_postId);
//    }

//    @Test
//    @DisplayName("ToDo 업데이트 실패 - 없는 postId")
//    void updateToDo_nothing() {
//        String dummy_postId = "dummy_postId";
//
//        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummy_content").isComplete(uncompleted).indexNum("1").build();
//        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().userId("dummy_userId").title("dummy_title").content(List.of(toDoRequestContentDto)).color(yellow).build();
//
//        Mockito.when(postRepository.findById(dummy_postId)).thenReturn(Optional.empty());
//
//        //System.out.println("값1 " + toDoResponsePostDto);
//
//        Assertions.assertThrows(RuntimeException.class, () -> toDoService.updateToDo(toDoRequestPostDto, dummy_postId));
//
//        verify(postRepository).findById(dummy_postId);
//    }
//
//    @Test
//    @DisplayName("Complete 값 변경 성공")
//    void changeComplete() {
//        String dummy_contentId = "dummy_contentId";
//        ContentEntity contentEntity = ContentEntity.builder().id("1").content("dummy_content").isComplete(ToDoEntityStatus.uncompleted).indexNum("1").build();
//
//        Mockito.when(contentRepository.findById(dummy_contentId)).thenReturn(Optional.of(contentEntity));
//        contentEntity.changeComplete();
//        Mockito.when(contentRepository.save(contentEntity)).thenReturn(contentEntity);
//
//        ToDoResponseContentDto toDoResponseContentDto = toDoService.changeComplete(dummy_contentId);
//        //System.out.println("값1 " + toDoResponsePostDto);
//
//        Assertions.assertEquals(toDoResponseContentDto, contentEntity.toDto());
//
//        verify(contentRepository).findById(dummy_contentId);
//        verify(contentRepository).save(contentEntity);
//    }
//
//    @Test
//    @DisplayName("Complete 값 변경 실패 - 없는 contentId")
//    void changeComplete_nothing() {
//        String dummy_contentId = "dummy_contentId";
//
//        Mockito.when(contentRepository.findById(dummy_contentId)).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(RuntimeException.class, () -> toDoService.changeComplete(dummy_contentId));
//
//        verify(contentRepository).findById(dummy_contentId);
//    }
//
//    @Test
//    void deleteToDo() {
//        String dummy_postId = "dummy_postId";
//
//        Mockito.doNothing().when(postRepository).deleteByPostId(dummy_postId);
//        //System.out.println("값1 " + toDoResponsePostDto);
//
//        //아무것도 안 return하기에 비교할게 없지만 호출은 해줘야함
//        toDoService.deleteToDo(dummy_postId);
//
//        verify(postRepository).deleteByPostId(dummy_postId);
//
//    }
}

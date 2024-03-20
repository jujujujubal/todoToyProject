package com.example.todo_back.service.impl;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.Role;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.dto.ToDoRequestContentDto;
import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.MemberEntity;
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

import static com.example.todo_back.data.constant.ColorList.YELLOW;
import static com.example.todo_back.data.constant.ToDoEntityStatus.UNCOMPLETED;
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

    @Test
    @DisplayName("ToDo 전부 받아오기")
    void getToDo() {
        ContentEntity contentEntity = ContentEntity.builder().id("content_id").content("내용").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();
        PostEntity postEntity = PostEntity.builder().postId("post_id").userId("TestPersonalId").userNickname("Test_nickname").title("dummy_title").color(ColorList.YELLOW).contents(List.of(contentEntity)).build();

        Mockito.when(postRepository.findAll(Sort.by(Sort.Direction.ASC, "createTime"))).thenReturn(List.of(postEntity));

        List<ToDoResponsePostDto> toDoResponsePostDtoList = toDoService.getToDo();

        Assertions.assertEquals(toDoResponsePostDtoList, List.of(postEntity.toDto()));

        verify(postRepository).findAll(Sort.by(Sort.Direction.ASC, "createTime"));
    }

    @Test
    @DisplayName("ToDo 저장 성공")
    void saveToDo() {
        MemberEntity memberEntity = MemberEntity.builder().personalId("TestPersonalId").password("Test_password").nickname("Test_nickname").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummyTitle").content(List.of(toDoRequestContentDto)).build();

        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("dummy_userId").userNickname("dummy_nickname").title("dummy_title").color(YELLOW).build();
        ContentEntity contentEntity = ContentEntity.builder().id("dummy_Id").post(postEntity).content("dummy_content").isComplete(UNCOMPLETED).indexNum("1").build();

        //randomUUID 로 인해 값이 항상 달라지기에 Mockito.any(PostEntity.class)로 처리함
        Mockito.when(memberRepository.findByPersonalId(memberEntity.getPersonalId())).thenReturn(Optional.of(memberEntity));
        Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);
        Mockito.when(contentRepository.save(Mockito.any(ContentEntity.class))).thenReturn(contentEntity);

        ToDoResponsePostDto toDoResponsePostDto = toDoService.saveToDo(toDoRequestPostDto, memberEntity.getPersonalId());

        //안 넣었던 거 넣어주기
        postEntity.setContents(List.of(contentEntity));

        Assertions.assertEquals(toDoResponsePostDto, postEntity.toDto());

        verify(memberRepository).findByPersonalId(memberEntity.getPersonalId());
        verify(postRepository).save(Mockito.any(PostEntity.class));
        verify(contentRepository).save(Mockito.any(ContentEntity.class));
    }

    @Test
    @DisplayName("ToDo 저장 실패 - 없는 personalId")
    void saveToDo_nothing() {
        MemberEntity memberEntity = MemberEntity.builder().personalId("TestPersonalId").password("Test_password").nickname("Test_nickname").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummyTitle").content(List.of(toDoRequestContentDto)).build();

        Mockito.when(memberRepository.findByPersonalId(memberEntity.getPersonalId())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.saveToDo(toDoRequestPostDto, memberEntity.getPersonalId()));

        verify(memberRepository).findByPersonalId(memberEntity.getPersonalId());
    }

    //업데이트라서 Mockito.when 을 통해 준 값을 수정하는데, 그게 사실 수정할 수 없는 값이라서 오류가 발생함
    @Test
    @DisplayName("ToDo 업데이트 성공")
    void updateToDo() {
        //수정 전
        ContentEntity contentEntity1 = ContentEntity.builder().id("content_id1").content("dummyContent1").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();
        PostEntity postEntity = PostEntity.builder().postId("post_id").userId("TestPersonalId").userNickname("Test_nickname").title("dummy_title").color(ColorList.YELLOW).contents(List.of(contentEntity1)).build();

        //이 내용으로 수정하기
        ToDoRequestContentDto toDoRequestContentDto1 = ToDoRequestContentDto.builder().content("dummyContent2").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestContentDto toDoRequestContentDto2 = ToDoRequestContentDto.builder().content("dummyContent3").isComplete(UNCOMPLETED).indexNum("2").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummy_title").content(List.of(toDoRequestContentDto1, toDoRequestContentDto2)).build();

        Mockito.when(postRepository.findById(postEntity.getPostId())).thenReturn(Optional.of(postEntity));

        //수정 후
        ContentEntity contentEntity2 = ContentEntity.builder().id("content_id2").content("dummyContent2").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();
        ContentEntity contentEntity3 = ContentEntity.builder().id("content_id3").content("dummyContent3").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("2").build();
        //postEntity.getContents().clear();
        postEntity.setContents(List.of(contentEntity2, contentEntity3));

        Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);

        ToDoResponsePostDto toDoResponsePostDto = toDoService.updateToDo(toDoRequestPostDto, postEntity.getPostId());
        //System.out.println("값1 " + toDoResponsePostDto);

        Assertions.assertEquals(toDoResponsePostDto, postEntity.toDto());

        verify(postRepository).findById(postEntity.getPostId());
        verify(postRepository).save(Mockito.any(PostEntity.class));
    }

    @Test
    @DisplayName("ToDo 업데이트 실패 - 없는 postId")
    void updateToDo_nothing() {
        String dummy_postId = "dummy_postId";

        ToDoRequestContentDto toDoRequestContentDto = ToDoRequestContentDto.builder().content("dummyContent").isComplete(UNCOMPLETED).indexNum("1").build();
        ToDoRequestPostDto toDoRequestPostDto = ToDoRequestPostDto.builder().title("dummy_title").content(List.of(toDoRequestContentDto)).build();

        Mockito.when(postRepository.findById(dummy_postId)).thenReturn(Optional.empty());

        //System.out.println("값1 " + toDoResponsePostDto);

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.updateToDo(toDoRequestPostDto, dummy_postId));

        verify(postRepository).findById(dummy_postId);
    }

    @Test
    @DisplayName("Complete 값 변경 성공")
    void changeComplete() {
        String dummy_contentId = "dummy_contentId";
        ContentEntity contentEntity = ContentEntity.builder().id("1").content("dummy_content").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();

        Mockito.when(contentRepository.findById(dummy_contentId)).thenReturn(Optional.of(contentEntity));
        contentEntity.changeComplete();
        Mockito.when(contentRepository.save(contentEntity)).thenReturn(contentEntity);

        ToDoResponseContentDto toDoResponseContentDto = toDoService.changeComplete(dummy_contentId);
        //System.out.println("값1 " + toDoResponsePostDto);

        Assertions.assertEquals(toDoResponseContentDto, contentEntity.toDto());

        verify(contentRepository).findById(dummy_contentId);
        verify(contentRepository).save(contentEntity);
    }

    @Test
    @DisplayName("Complete 값 변경 실패 - 없는 contentId")
    void changeComplete_nothing() {
        String dummy_contentId = "dummy_contentId";

        Mockito.when(contentRepository.findById(dummy_contentId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.changeComplete(dummy_contentId));

        verify(contentRepository).findById(dummy_contentId);
    }

    @Test
    void deleteToDo() {
        String dummy_postId = "dummy_postId";

        Mockito.doNothing().when(postRepository).deleteByPostId(dummy_postId);
        //System.out.println("값1 " + toDoResponsePostDto);

        //아무것도 안 return하기에 비교할게 없지만 호출은 해줘야함
        toDoService.deleteToDo(dummy_postId);

        verify(postRepository).deleteByPostId(dummy_postId);

    }
}

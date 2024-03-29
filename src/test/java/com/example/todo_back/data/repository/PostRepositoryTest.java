package com.example.todo_back.data.repository;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void testContentEntity() {
        ContentEntity contentEntity = ContentEntity.builder().id("dummy_id").content("dummy_content").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();

        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("admin").userNickname("dummy_nickname").title("dummy_title").color(ColorList.YELLOW).contents(List.of(contentEntity)).build();
        postRepository.save(postEntity);

        PostEntity findPostEntity = postRepository.findById(postEntity.getPostId()).get();

        Assertions.assertEquals(postEntity, findPostEntity);
    }

    @Test
    void basicCRUD() {
        ContentEntity contentEntity1 = ContentEntity.builder().id("dummy1_id").content("dummy1_content").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();
        ContentEntity contentEntity2 = ContentEntity.builder().id("dummy2_id").content("dummy2_content").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("2").build();

        PostEntity postEntity1 = PostEntity.builder().postId("dummy1_postId").userId("admin").userNickname("dummy_nickname").title("dummy_title1").color(ColorList.YELLOW).contents(List.of(contentEntity1)).build();
        PostEntity postEntity2 = PostEntity.builder().postId("dummy2_postId").userId("admin").userNickname("dummy_nickname").title("dummy_title2").color(ColorList.YELLOW).contents(List.of(contentEntity2)).build();

        postRepository.save(postEntity1);
        postRepository.save(postEntity2);

        //단건 조회 검증
        PostEntity findPostEntity1 = postRepository.findById(postEntity1.getPostId()).get();
        PostEntity findPostEntity2 = postRepository.findById(postEntity2.getPostId()).get();

        Assertions.assertEquals(postEntity1, findPostEntity1);
        Assertions.assertEquals(postEntity2, findPostEntity2);

        findPostEntity1.setTitle("수정 후 title 내용");

        //리스트 조회 검증
        List<PostEntity> allPostEntity = postRepository.findAll();
        Assertions.assertEquals(2, allPostEntity.size());

        //setContent가 잘 반영되었음을 알 수 있다
        //System.out.println("수정된 내용" + allPostEntity);

        //카운트 검증
        long count = postRepository.count();
        Assertions.assertEquals(2, count);

        //삭제 검증
        postRepository.delete(postEntity1);
        postRepository.delete(postEntity2);

        long afterDeleteCount = postRepository.count();
        Assertions.assertEquals(0, afterDeleteCount);
    }

    @Test
    void deleteByPostId() {
        //만들고 저장
        ContentEntity contentEntity = ContentEntity.builder().id("dummy_id").content("dummy_content").isComplete(ToDoEntityStatus.UNCOMPLETED).indexNum("1").build();
        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("admin").userNickname("dummy_nickname").title("dummy_title").color(ColorList.YELLOW).contents(List.of(contentEntity)).build();
        postRepository.save(postEntity);

        //잘 저장됐는지 확인
        PostEntity findPostEntity = postRepository.findById(postEntity.getPostId()).get();
        Assertions.assertEquals(postEntity, findPostEntity);
        long afterSaveCount = postRepository.count();
        Assertions.assertEquals(1, afterSaveCount);

        //삭제하기 및 확인
        postRepository.deleteByPostId(postEntity.getPostId());
        long afterDeleteCount = postRepository.count();
        Assertions.assertEquals(0, afterDeleteCount);
    }
}

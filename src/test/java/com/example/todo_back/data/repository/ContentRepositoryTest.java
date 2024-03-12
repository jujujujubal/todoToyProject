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
class ContentRepositoryTest {

    @Autowired
    ContentRepository contentRepository;

    //postRepository 를 Autowired 하는게 맞나?? 목빈으로 만들어야하나???
    @Autowired
    PostRepository postRepository;

    @Test
    public void testContentEntity() {
        LocalDateTime localDateTime = LocalDateTime.now();
        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("admin").title("dummy_title").createTime(localDateTime).color(ColorList.yellow).build();
        postRepository.save(postEntity);
        ContentEntity contentEntity = ContentEntity.builder().id("dummy_id").post(postEntity).content("dummy_content").isComplete(ToDoEntityStatus.uncompleted).indexNum("1").build();
        contentRepository.save(contentEntity);

        ContentEntity findContentEntity = contentRepository.findById(contentEntity.getId()).get();

        Assertions.assertEquals(findContentEntity.getId(), contentEntity.getId());
        Assertions.assertEquals(findContentEntity.getPost(), contentEntity.getPost());
        Assertions.assertEquals(findContentEntity.getContent(), contentEntity.getContent());
        Assertions.assertEquals(findContentEntity.getIsComplete(), contentEntity.getIsComplete());
        Assertions.assertEquals(findContentEntity.getIndexNum(), contentEntity.getIndexNum());
        Assertions.assertEquals(findContentEntity, contentEntity);
    }

    @Test
    public void basicCRUD() {
        LocalDateTime localDateTime = LocalDateTime.now();
        PostEntity postEntity = PostEntity.builder().postId("dummy_postId").userId("admin").title("dummy_title").createTime(localDateTime).color(ColorList.yellow).build();
        postRepository.save(postEntity);

        ContentEntity contentEntity1 = ContentEntity.builder().id("dummy1_id").post(postEntity).content("dummy1_content").isComplete(ToDoEntityStatus.uncompleted).indexNum("1").build();
        ContentEntity contentEntity2 = ContentEntity.builder().id("dummy2_id").post(postEntity).content("dummy2_content").isComplete(ToDoEntityStatus.uncompleted).indexNum("2").build();

        contentRepository.save(contentEntity1);
        contentRepository.save(contentEntity2);

        //단건 조회 검증
        ContentEntity findContentEntity1 = contentRepository.findById(contentEntity1.getId()).get();
        ContentEntity findContentEntity2 = contentRepository.findById(contentEntity2.getId()).get();

        Assertions.assertEquals(findContentEntity1, contentEntity1);
        Assertions.assertEquals(findContentEntity2, contentEntity2);

        findContentEntity1.setContent("수정 후 content 내용");

        //리스트 조회 검증
        List<ContentEntity> allContentEntity = contentRepository.findAll();
        Assertions.assertEquals(allContentEntity.size(), 2);

        //setContent가 잘 반영되었음을 알 수 있다
        System.out.println("수정된 내용" + allContentEntity);

        //카운트 검증
        long count = contentRepository.count();
        Assertions.assertEquals(count, 2);

        //삭제 검증
        contentRepository.delete(contentEntity1);
        contentRepository.delete(contentEntity2);

        long afterDeleteCount = contentRepository.count();
        Assertions.assertEquals(afterDeleteCount, 0);
    }

}

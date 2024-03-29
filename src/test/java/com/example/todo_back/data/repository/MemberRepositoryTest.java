package com.example.todo_back.data.repository;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.Role;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import com.example.todo_back.data.entity.ContentEntity;
import com.example.todo_back.data.entity.MemberEntity;
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
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testContentEntity() {
        MemberEntity memberEntity = MemberEntity.builder().personalId("dummyPersonalId").password("dummy_password").nickname("admin").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        memberRepository.save(memberEntity);

        MemberEntity findMemberEntity = memberRepository.findById(memberEntity.getPersonalId()).get();

        Assertions.assertEquals(memberEntity, findMemberEntity);
    }

    @Test
    void basicCRUD() {
        MemberEntity memberEntity1 = MemberEntity.builder().personalId("dummyPersonalId1").password("dummy_password").nickname("admin1").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        MemberEntity memberEntity2 = MemberEntity.builder().personalId("dummyPersonalId2").password("dummy_password").nickname("admin2").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        memberRepository.save(memberEntity1);
        memberRepository.save(memberEntity2);

        //단건 조회 검증
        MemberEntity findMemberEntity1 = memberRepository.findById(memberEntity1.getPersonalId()).get();
        MemberEntity findMemberEntity2 = memberRepository.findById(memberEntity2.getPersonalId()).get();

        Assertions.assertEquals(memberEntity1, findMemberEntity1);
        Assertions.assertEquals(memberEntity2, findMemberEntity2);

        findMemberEntity1.setNickname("변경된 닉네임");

        //리스트 조회 검증
        List<MemberEntity> allMemberEntity = memberRepository.findAll();
        Assertions.assertEquals(2, allMemberEntity.size());

        //setContent가 잘 반영되었음을 알 수 있다
        //System.out.println("수정된 닉네임" + allMemberEntity);

        //카운트 검증
        long count = memberRepository.count();
        Assertions.assertEquals(2, count);

        //삭제 검증
        memberRepository.delete(memberEntity1);
        memberRepository.delete(memberEntity2);

        long afterDeleteCount = memberRepository.count();
        Assertions.assertEquals(0, afterDeleteCount);
    }

    @Test
    void findByPersonalId() {
        MemberEntity memberEntity = MemberEntity.builder().personalId("dummyPersonalId").password("dummy_password").nickname("admin").role(Role.ROLE_USER).color(ColorList.YELLOW).build();
        memberRepository.save(memberEntity);

        MemberEntity findMemberEntity = memberRepository.findByPersonalId(memberEntity.getPersonalId()).get();

        Assertions.assertEquals(memberEntity, findMemberEntity);
    }
}

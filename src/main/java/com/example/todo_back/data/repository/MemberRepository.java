package com.example.todo_back.data.repository;

import com.example.todo_back.data.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Optional<MemberEntity> findByPersonalId(String personalId);
}

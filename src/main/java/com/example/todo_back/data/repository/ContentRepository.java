package com.example.todo_back.data.repository;

import com.example.todo_back.data.entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, String> {
}

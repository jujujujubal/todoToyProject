package com.example.todo_back.data.repository;

import com.example.todo_back.data.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, String> {
    void deleteByPostId(String postId);
}

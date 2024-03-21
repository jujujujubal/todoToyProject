package com.example.todo_back.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponsePostDto {
    private String postId;
    private String userNickname;
    private String title;
    private List<ToDoResponseContentDto> content;
    private LocalDateTime createTime;
    private String color;
}

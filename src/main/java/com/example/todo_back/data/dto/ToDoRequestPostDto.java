package com.example.todo_back.data.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoRequestPostDto {
    private String title;
    private List<ToDoRequestContentDto> content;
}

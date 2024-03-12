package com.example.todo_back.data.dto;
import com.example.todo_back.data.constant.ToDoEntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponseContentDto {
    private String id;
    private String content;
    private ToDoEntityStatus isComplete;
}

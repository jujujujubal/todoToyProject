package com.example.todo_back.service;

import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ToDoService {
    List<ToDoResponsePostDto> getToDo();
    ToDoResponsePostDto saveToDo(ToDoRequestPostDto toDoRequestPostDto);

    ToDoResponsePostDto updateToDo(ToDoRequestPostDto toDoRequestPostDto, String postId);

    ToDoResponseContentDto changeComplete(String id);

    void deleteToDo(String postId);
}

package com.example.todo_back.service;

import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;

import java.util.List;

public interface ToDoService {
    List<ToDoResponsePostDto> getToDo();
    ToDoResponsePostDto saveToDo(ToDoRequestPostDto toDoRequestPostDto, String userPersonalId);

    ToDoResponsePostDto updateToDo(ToDoRequestPostDto toDoRequestPostDto, String postId);

    ToDoResponseContentDto changeComplete(String id);

    void deleteToDo(String postId);
}

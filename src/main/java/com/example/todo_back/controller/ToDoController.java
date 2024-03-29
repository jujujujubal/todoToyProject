package com.example.todo_back.controller;

import com.example.todo_back.data.dto.ToDoRequestPostDto;
import com.example.todo_back.data.dto.ToDoResponseContentDto;
import com.example.todo_back.data.dto.ToDoResponsePostDto;
import com.example.todo_back.data.entity.MemberEntity;
import com.example.todo_back.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.validation.Valid;

import java.util.List;

@Slf4j
@RestController
//@CrossOrigin("*")
@RequestMapping("/api/v1/todo")
public class ToDoController {
    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService){
        this.toDoService = toDoService;
    }

    //모든 포스트잇들을 받아오기
    @GetMapping(value = "/load")
    public ResponseEntity<List<ToDoResponsePostDto>> getToDo(){
        List<ToDoResponsePostDto> toDoResponsePostDtoList = toDoService.getToDo();
        return ResponseEntity.status(HttpStatus.OK).body(toDoResponsePostDtoList);
    }

    //투두 내용을 받기. (작성시간, 작성자 아이디, 타이틀, 소내용(배열)(완료 여부, 소제목))
    @PostMapping(value = "/create")
    public ResponseEntity<ToDoResponsePostDto> createToDo(@Valid @RequestBody ToDoRequestPostDto toDoRequestPostDto){
        MemberEntity memberEntity = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ToDoResponsePostDto toDoResponsePostDto = toDoService.saveToDo(toDoRequestPostDto, memberEntity.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(toDoResponsePostDto);
    }

    @PutMapping(value = "/update/{postId}")
    public ResponseEntity<ToDoResponsePostDto> updatePost(@Valid @RequestBody ToDoRequestPostDto toDoRequestPostDto, @Valid @PathVariable String postId){
        ToDoResponsePostDto toDoResponsePostDto1 = toDoService.updateToDo(toDoRequestPostDto, postId);
        return ResponseEntity.status(HttpStatus.OK).body(toDoResponsePostDto1);
    }

    @PutMapping(value = "/update/content/{id}")
    public ResponseEntity<ToDoResponseContentDto> changeComplete(@Valid @PathVariable String id){
        ToDoResponseContentDto toDoResponseContentDto = toDoService.changeComplete(id);
        return ResponseEntity.status(HttpStatus.OK).body(toDoResponseContentDto);
    }

    //http://localhost:8080/api/v1/todo/{id 값}
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deleteToDo(@PathVariable String postId) {
        toDoService.deleteToDo(postId);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}

package com.example.todo_back.controller;

import com.example.todo_back.data.dto.*;
import com.example.todo_back.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin("http://localhost:8081")
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    //회원가입
    @PostMapping("/signUp")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody SignUpDto signUpDto){
        Map<String, Object> responseObject = new HashMap<>();
        try {
            Boolean singUpSuccess = authService.signUp(signUpDto);
            responseObject.put("success", true);
            responseObject.put("error_code", 0);
            responseObject.put("item", singUpSuccess);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (IllegalArgumentException e){
            responseObject.put("success", false);
            responseObject.put("error_code", 409);
            responseObject.put("item", "존재하는 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObject);
        }
    }

    //로그인 시도하기
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult){
        Map<String, Object> responseObject = new HashMap<>();

        if (bindingResult.hasErrors()){
            responseObject.put("success", false);
            responseObject.put("error_code", 400);
            responseObject.put("item", "입력 형식이 알맞지 않습니다.");
            //loginDto 가 @Valid 하지 않을 때 이 안으로 들어옴
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
        }

        try {
            String loginSuccess = authService.login(loginDto);
            responseObject.put("success", true);
            responseObject.put("error_code", 0);
            responseObject.put("item", loginSuccess);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (IllegalArgumentException e){
            responseObject.put("success", false);
            responseObject.put("error_code", 404);
            responseObject.put("item", "일치하는 회원이 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        }

    }
}

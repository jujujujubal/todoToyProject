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
    private static final String SUCCESS = "success";
    private static final String ERROR_CODE = "error_code";
    private static final String ITEM = "item";

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
            responseObject.put(SUCCESS, true);
            responseObject.put(ERROR_CODE, 0);
            responseObject.put(ITEM, singUpSuccess);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (IllegalArgumentException e){
            responseObject.put(SUCCESS, false);
            responseObject.put(ERROR_CODE, 409);
            responseObject.put(ITEM, "존재하는 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObject);
        }
    }

    //로그인 시도하기
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult){
        Map<String, Object> responseObject = new HashMap<>();

        if (bindingResult.hasErrors()){
            responseObject.put(SUCCESS, false);
            responseObject.put(ERROR_CODE, 400);
            responseObject.put(ITEM, "입력 형식이 알맞지 않습니다.");
            //loginDto 가 @Valid 하지 않을 때 이 안으로 들어옴
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
        }

        try {
            String loginSuccess = authService.login(loginDto);
            responseObject.put(SUCCESS, true);
            responseObject.put(ERROR_CODE, 0);
            responseObject.put(ITEM, loginSuccess);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);

        } catch (IllegalArgumentException e){
            responseObject.put(SUCCESS, false);
            responseObject.put(ERROR_CODE, 404);
            responseObject.put(ITEM, "일치하는 회원이 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        }

    }
}

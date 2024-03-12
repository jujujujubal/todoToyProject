package com.example.todo_back.service;

import com.example.todo_back.data.dto.LoginDto;
import com.example.todo_back.data.dto.SignUpDto;

public interface AuthService {
    Boolean signUp(SignUpDto signUpDto);

    String login(LoginDto loginDto);
}

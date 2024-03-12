package com.example.todo_back.service.impl;

import com.example.todo_back.data.dto.LoginDto;
import com.example.todo_back.data.dto.SignUpDto;
import com.example.todo_back.data.entity.MemberEntity;
import com.example.todo_back.data.repository.MemberRepository;
import com.example.todo_back.jwt.JwtTokenProvider;
import com.example.todo_back.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Boolean signUp(SignUpDto signUpDto){
        //이미 존재하는 아이디를 입력한 경우
        if(memberRepository.findByPersonalId(signUpDto.getPersonalId()).isPresent()){
            throw new RuntimeException("존재하는 아이디입니다.");
        }
        // 아이디 형식이 올바르지 않은 경우 (@Valid 로 400에러가 뜸)
        // 패스워드 형식이 올바르지 않은 경우 (@Valid 로 400에러가 뜸)
        // 닉네임 형식이 올바르지 않은 경우
        // 올바른 경우
        MemberEntity memberEntity = memberRepository.save(signUpDto.toEntity());
        memberEntity.passwordEncode(passwordEncoder);
        memberEntity.addUserAuthority();
        return true;
    }

    @Override
    public String login(LoginDto loginDto){
        MemberEntity memberEntity = memberRepository.findByPersonalId(loginDto.getPersonalId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
        if (!passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String role = memberEntity.getRole().name();
        return jwtTokenProvider.createToken(memberEntity.getPersonalId(), role, memberEntity.getNickname());
    }
}

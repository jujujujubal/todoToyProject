package com.example.todo_back.jwt;

import com.example.todo_back.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException{
        return memberRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new  IllegalArgumentException("입력하신 아이디와 일치하는 정보가 없습니다."));
    }
}

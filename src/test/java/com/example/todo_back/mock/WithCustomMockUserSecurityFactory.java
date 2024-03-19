package com.example.todo_back.mock;

import com.example.todo_back.data.entity.MemberEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithCustomMockUserSecurityFactory implements WithSecurityContextFactory<WithCustomMockUser>{

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser customUser){
        MemberEntity member = MemberEntity.builder().personalId(customUser.personalId()).password(customUser.password()).nickname(customUser.nickname()).role(customUser.role()).color(customUser.color()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(member, member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole().name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        return context;
    }

}

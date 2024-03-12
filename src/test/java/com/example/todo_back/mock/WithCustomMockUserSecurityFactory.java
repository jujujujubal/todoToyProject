package com.example.todo_back.mock;

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
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<GrantedAuthority> authorities = Arrays.stream(customUser.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(customUser.personalId(), "password", authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", authorities);
        context.setAuthentication(auth);

        return context;

    }

}

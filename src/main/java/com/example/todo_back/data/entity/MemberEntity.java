package com.example.todo_back.data.entity;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class MemberEntity implements UserDetails {
    @Id
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-]{8,}$")
    private String personalId;

    @NotEmpty
    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private ColorList color;

    public void addUserAuthority() {
        this.role = Role.User;
    }
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(role.name()));
        return auth;
    }

    @Override
    public String getUsername(){
        return personalId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

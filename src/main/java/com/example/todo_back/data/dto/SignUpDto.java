package com.example.todo_back.data.dto;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-]{8,12}$")
    private String personalId;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-]{8,12}$")
    private String password;

    private String nickname;

    private ColorList color;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .personalId(personalId)
                .password(password)
                .nickname(nickname)
                .color(color)
                .build();
    }
}

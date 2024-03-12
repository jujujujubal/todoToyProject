package com.example.todo_back.data.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @Id
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-]{8,12}$")
    private String personalId;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-]{8,12}$")
    private String password;
}

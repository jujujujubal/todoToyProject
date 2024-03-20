package com.example.todo_back.mock;

import com.example.todo_back.data.constant.ColorList;
import com.example.todo_back.data.constant.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityFactory.class)
public @interface WithCustomMockUser {
    String personalId() default "TestPersonalId";
    String password() default "Test_password";
    String nickname() default "Test_nickname";
    Role role() default Role.ROLE_USER;
    ColorList color() default ColorList.YELLOW;
}

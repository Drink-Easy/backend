package com.drinkeg.drinkeg;

import com.drinkeg.drinkeg.domain.SecurityContextFactory;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SecurityContextFactory.class)
public @interface MockMember {
    String username() default "user";
    String password() default "password";
    Role role() default Role.ROLE_USER;
}

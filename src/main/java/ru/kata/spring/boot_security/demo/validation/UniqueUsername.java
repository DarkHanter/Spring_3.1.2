package ru.kata.spring.boot_security.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
public @interface UniqueUsername {
    String message() default "User with this username already existed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {

    };
}

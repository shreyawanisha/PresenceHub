package org.attendance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NortheasternEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NortheasternEmail {
    String message() default "Email must be a northeastern.edu address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
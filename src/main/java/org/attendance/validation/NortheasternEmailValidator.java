package org.attendance.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NortheasternEmailValidator implements ConstraintValidator<NortheasternEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && email.endsWith("@northeastern.edu");
    }
}

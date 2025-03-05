package dev.datpl.commonservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidRoleValidator.class)
@Documented
public @interface ValidRole {
    String message() default "Invalid role. Allowed roles are: trainee, trainer, admin, partner";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class ValidRoleValidator implements ConstraintValidator<ValidRole, String> {
    private static final Set<String> VALID_ROLES = Set.of("trainee", "trainer", "admin", "partner");

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null) {
            return false;
        }
        return VALID_ROLES.contains(role);
    }
}

package ParentHiveApp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConditionalProfessionalCertificationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalProfessionalCertification {
    String message() default "Certification is required for professionals";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package ParentHiveApp.validation;

import ParentHiveApp.model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ParentHiveApp.dto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

public class ConditionalProfessionalCertificationValidator implements ConstraintValidator<ConditionalProfessionalCertification, UserRegistrationDto> {

    @Override
    public boolean isValid(UserRegistrationDto dto, ConstraintValidatorContext context) {
        // If role is PROFESSIONAL, certification file must be present
        if (dto.getRole() == Role.PROFESSIONAL) {
            MultipartFile file = dto.getCertificationFile();
            String certification = dto.getCertification();
            if (file == null || file.isEmpty()&& (certification == null || certification.trim().isEmpty())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Either certification text or file is required for professional accounts")
                        .addPropertyNode("certification")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}

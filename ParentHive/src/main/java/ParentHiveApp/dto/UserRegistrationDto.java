package ParentHiveApp.dto;

import ParentHiveApp.model.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ParentHiveApp.validation.PasswordMatches;
import ParentHiveApp.validation.ConditionalProfessionalCertification;

@Getter
@Setter
@PasswordMatches
@ConditionalProfessionalCertification
public class UserRegistrationDto {

    @NotBlank
    @Size(min = 6, max = 20)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String selectedRole;

    private String certification;

    private MultipartFile certificationFile;
}

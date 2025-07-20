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

    public @NotBlank @Size(min = 6, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 6, max = 20) String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8) String password) {
        this.password = password;
    }

    public @NotBlank String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @NotBlank String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(@NotBlank String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public MultipartFile getCertificationFile() {
        return certificationFile;
    }

    public void setCertificationFile(MultipartFile certificationFile) {
        this.certificationFile = certificationFile;
    }
}

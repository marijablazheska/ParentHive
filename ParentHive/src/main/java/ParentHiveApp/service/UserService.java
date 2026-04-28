package ParentHiveApp.service;

import ParentHiveApp.dto.UserRegistrationDto;
import ParentHiveApp.model.User;

import java.util.Optional;

public interface UserService {
    Long getCurrentUserId();
    Optional<User> getUserById(Long id);
    void registerUser(UserRegistrationDto dto);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean updatePassword(Long id, String newPassword, String newConfPassword);
    boolean deleteUser(Long id, String newPassword);
}

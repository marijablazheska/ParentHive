package ParentHiveApp.service;

import ParentHiveApp.model.User;

import java.util.Optional;

public interface UserService {
    Long getCurrentUserId();
    Optional<User> getUserById(Long id);
}

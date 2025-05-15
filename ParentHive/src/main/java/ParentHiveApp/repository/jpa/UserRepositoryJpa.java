package ParentHiveApp.repository.jpa;


import ParentHiveApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {
// TODO
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}

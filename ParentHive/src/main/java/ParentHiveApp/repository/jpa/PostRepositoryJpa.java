package ParentHiveApp.repository.jpa;

import ParentHiveApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositoryJpa extends JpaRepository<Post, Long> {
    // TODO
}

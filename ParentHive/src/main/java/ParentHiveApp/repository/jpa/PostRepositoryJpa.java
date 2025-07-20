package ParentHiveApp.repository.jpa;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryJpa extends JpaRepository<Post, Long> {
    Post getPostById(Long id);
    List<Post> findAllByTitleContainingIgnoreCase(String title);
    List<Post> findAllByCategoryIgnoreCase(String category);
    List<Post> findAllByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCase(String title, String category);
//    Post getPostByTitleAndCategory(String title, List<String> category);

}

package ParentHiveApp.repository.jpa;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryJpa extends JpaRepository<Post, Long> {
    // TODO
    Post getPostById(Long id);
    Post getPostByTitle(String title);
    Post getPostByTitleIgnoreCaseAndCategoryIgnoreCase(String title, String category);
    List<Reply> findByPostId(Long postId);


//    Post getPostByTitleAndCategory(String title, List<String> category);

}

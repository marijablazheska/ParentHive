package ParentHiveApp.repository.jpa;

import ParentHiveApp.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepositoryJpa extends JpaRepository<Reply, Long> {
//    TODO
    Reply findById(long id);
    List<Reply> findByPostId(Long postId);
}

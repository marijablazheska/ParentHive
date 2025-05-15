package ParentHiveApp.repository.jpa;

import ParentHiveApp.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepositoryJpa extends JpaRepository<Reply, Long> {
//    TODO
    Reply findById(long id);
    List<Reply> findByPostId(Long postId);
}

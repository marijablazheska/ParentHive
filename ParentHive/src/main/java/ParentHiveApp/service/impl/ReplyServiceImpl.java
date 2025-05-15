package ParentHiveApp.service.impl;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
import ParentHiveApp.repository.jpa.ReplyRepositoryJpa;
import ParentHiveApp.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepositoryJpa replyRepositoryJpa;

    public ReplyServiceImpl(ReplyRepositoryJpa replyRepositoryJpa) {
        this.replyRepositoryJpa = replyRepositoryJpa;
    }

    @Override
    public Reply findById(long id) {
        return replyRepositoryJpa.findById(id);
    }

    @Override
    public List<Reply> listAllRepliesById(Long postId) {
        return replyRepositoryJpa.findByPostId(postId);
    }

}

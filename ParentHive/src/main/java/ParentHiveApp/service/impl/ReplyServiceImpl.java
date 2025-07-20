package ParentHiveApp.service.impl;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.ReplyRepositoryJpa;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepositoryJpa replyRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    public ReplyServiceImpl(ReplyRepositoryJpa replyRepositoryJpa, UserRepositoryJpa userRepositoryJpa) {
        this.replyRepositoryJpa = replyRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public Reply findById(long id) {
        return replyRepositoryJpa.findById(id);
    }

    @Override
    public Reply updateReply(Long id, String Content) {
        Optional<Reply> oldReply = replyRepositoryJpa.findById(id);
        Reply newReply = oldReply.orElseGet(Reply::new);

        newReply.setContent(Content);

        if(Content.contains("cochrane.org") || Content.contains("embs.org") || Content.contains("pubmed.ncbi.nlm.nih.gov")) {
            if(newReply.getUser().getProfessional()) {
                newReply.setPost_level(2);
            } else {
                newReply.setPost_level(1);
            }
        } else {
            newReply.setPost_level(0);
        }

        replyRepositoryJpa.save(newReply);
        return newReply;
    }

    @Override
    public void delete(long id) {
        replyRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Reply> listReplies() {
        return replyRepositoryJpa.findAll();
    }

    @Override
    public List<Reply> listAllRepliesById(Long postId) {
        return replyRepositoryJpa.findByPostId(postId);
    }

    @Override
    public Reply createReply(String Content, Optional<User> user, Post post) {
        Reply reply = new Reply(Content, user.get(), post);
        replyRepositoryJpa.save(reply);

        return reply;
    }

    @Override
    public void upvoteReply(Long replyId, Optional<User> user) {
        if(user.isPresent()) {
            if(user.get().getUpVotedReplies().contains(replyId)){
                user.get().getUpVotedReplies().remove(replyId);
                userRepositoryJpa.save(user.get());

                Reply reply = replyRepositoryJpa.findById(replyId).get();
                reply.setUpvote(reply.getUpvote() - 1);
                replyRepositoryJpa.save(reply);
            } else {
                user.get().getUpVotedReplies().add(replyId);
                userRepositoryJpa.save(user.get());

                Reply reply = replyRepositoryJpa.findById(replyId).get();
                reply.setUpvote(reply.getUpvote() + 1);
                replyRepositoryJpa.save(reply);
            }
        }
    }

    @Override
    public void downvoteReply(Long replyId, Optional<User> user) {
        if(user.isPresent()) {
            if(user.get().getDownVotedReplies().contains(replyId)){
                user.get().getDownVotedReplies().remove(replyId);
                userRepositoryJpa.save(user.get());

                Reply reply = replyRepositoryJpa.findById(replyId).get();
                reply.setDownvote(reply.getDownvote() - 1);
                replyRepositoryJpa.save(reply);
            } else {
                user.get().getDownVotedReplies().add(replyId);
                userRepositoryJpa.save(user.get());

                Reply reply = replyRepositoryJpa.findById(replyId).get();
                reply.setDownvote(reply.getDownvote() + 1);
                replyRepositoryJpa.save(reply);
            }
        }
    }

}

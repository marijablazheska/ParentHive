package ParentHiveApp.service;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
import ParentHiveApp.model.User;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
//    TODO
    Reply findById(long id);
//    void save(String content);
    Reply updateReply(Long id, String Content);
    void delete(long id);
    List<Reply> listReplies();
    List<Reply> listAllRepliesById(Long id);
    Reply createReply(String Content, Optional<User> user, Post post);
    void upvoteReply(Long replyId, Optional<User> user);
    void downvoteReply(Long replyId, Optional<User> user);
}

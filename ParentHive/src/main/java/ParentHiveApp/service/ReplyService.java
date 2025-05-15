package ParentHiveApp.service;

import ParentHiveApp.model.Reply;

import java.util.List;

public interface ReplyService {
//    TODO
    Reply findById(long id);
//    void save(String content);
//    void delete(long id);
    List<Reply> listAllRepliesById(Long id);
}

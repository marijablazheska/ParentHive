package ParentHiveApp.service;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
//    TODO
    Post getPostById(Long id);
    List<Post> getPostByTitle(String title);
    List<Post> getPostByCategory(String category);
    List<Post> getPostByTitleAndCategory(String title, String category);
    Post createPost(String title, String content, String category, Optional<User> user); // Save
    Post updatePost(Long id, String title, String content, String category); // Save
    void delete(Long id); // Delete
    List<Post> listPosts();
    void upvotePost(Long postId, Optional<User> user);
    void downvotePost(Long postId, Optional<User> user);
    void repostPost(Long postId, Optional<User> user);

}

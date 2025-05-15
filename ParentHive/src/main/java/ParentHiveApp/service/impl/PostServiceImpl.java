package ParentHiveApp.service.impl;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.PostRepositoryJpa;
import ParentHiveApp.repository.jpa.ReplyRepositoryJpa;
import ParentHiveApp.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepositoryJpa postRepositoryJpa;
    private final ReplyRepositoryJpa replyRepositoryJpa;

    public PostServiceImpl(PostRepositoryJpa postRepositoryJpa, ReplyRepositoryJpa replyRepositoryJpa) {
        this.postRepositoryJpa = postRepositoryJpa;
        this.replyRepositoryJpa = replyRepositoryJpa;
    }


    @Override
    public Post getPostById(Long id) {
        return postRepositoryJpa.getPostById(id);
    }

    @Override
    public Post getPostByTitle(String title) {
        return postRepositoryJpa.getPostByTitle(title);
    }

    @Override
    public Post getPostByTitleAndCategory(String title, String category) {
        return postRepositoryJpa.getPostByTitleIgnoreCaseAndCategoryIgnoreCase(title, category);
    }
    @Transactional
    @Override
    public Post createPost(String title, String content, String category, Optional<User> user) {
        Post newPost = new Post(title, content, category, user.get());
        postRepositoryJpa.save(newPost);

        return newPost;
    }

    @Transactional
    @Override
    public Post updatePost(Long id, String title, String content, String category) {
        Post oldPost = postRepositoryJpa.getPostById(id);

        oldPost.setTitle(title);
        oldPost.setContent(content);
        oldPost.setCategory(category);

        postRepositoryJpa.save(oldPost);
        return oldPost;
    }
    @Transactional
    @Override
    public void delete(Long id) {
        postRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Post> listPosts() {
        return postRepositoryJpa.findAll();
    }

}

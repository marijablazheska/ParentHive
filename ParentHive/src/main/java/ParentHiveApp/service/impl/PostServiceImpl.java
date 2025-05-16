package ParentHiveApp.service.impl;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.PostRepositoryJpa;
import ParentHiveApp.repository.jpa.ReplyRepositoryJpa;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepositoryJpa postRepositoryJpa;
    private final ReplyRepositoryJpa replyRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    public PostServiceImpl(PostRepositoryJpa postRepositoryJpa, ReplyRepositoryJpa replyRepositoryJpa, UserRepositoryJpa userRepositoryJpa) {
        this.postRepositoryJpa = postRepositoryJpa;
        this.replyRepositoryJpa = replyRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
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

    @Override
    public void upvotePost(Long postId, Optional<User> user) {
        if(user.isPresent()) {
            if(user.get().getUpVotedPosts().contains(postId)){
                user.get().getUpVotedPosts().remove(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.setUpvote(post.getUpvote() - 1);
                postRepositoryJpa.save(post);
            } else {
                user.get().getUpVotedPosts().add(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.incrementUpVote();
                postRepositoryJpa.save(post);
            }
        }

    }

    @Override
    public void downvotePost(Long postId, Optional<User> user) {
        if(user.isPresent()) {
            if(user.get().getDownVotedPosts().contains(postId)){
                user.get().getDownVotedPosts().remove(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.setDownvote(post.getDownvote() - 1);
                postRepositoryJpa.save(post);
            } else {
                user.get().getDownVotedPosts().add(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.incrementDownVote();
                postRepositoryJpa.save(post);
            }
        }

    }

    @Override
    public void repostPost(Long postId, Optional<User> user) {
        if(user.isPresent()) {
            if(user.get().getRepostedPosts().contains(postId)){
                user.get().getRepostedPosts().remove(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.setRepost(post.getRepost() - 1);
                postRepositoryJpa.save(post);
            } else {
                user.get().getRepostedPosts().add(postId);
                userRepositoryJpa.save(user.get());

                Post post = postRepositoryJpa.getPostById(postId);
                post.incrementRepost();
                postRepositoryJpa.save(post);
            }
        }
    }

}

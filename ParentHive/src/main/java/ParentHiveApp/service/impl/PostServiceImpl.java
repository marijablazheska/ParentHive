package ParentHiveApp.service.impl;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.PostRepositoryJpa;
import ParentHiveApp.repository.jpa.ReplyRepositoryJpa;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Post> getPostByTitle(String title) {
        return postRepositoryJpa.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Post> getPostByCategory(String category) {
        return postRepositoryJpa.findAllByCategoryContainingIgnoreCase(category);
    }

    @Override
    public List<Post> getPostByTitleAndCategory(String title, String category) {
        return postRepositoryJpa.findAllByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCase(title, category);
    }

    @Override
    public List<Post> sortBy(String sortBy, List<Post> posts) {

        Comparator<Post> comparator = Comparator.comparing(Post::getDate);


        if(sortBy.equals("Newest")){
            posts.sort(comparator.reversed());
        } else if(sortBy.equals("Oldest")){
            posts.sort(comparator);
        } else if(sortBy.equals("Upvotes")){
            return posts.stream().sorted(Comparator.comparingInt(Post::getUpvote).reversed()).toList();
        } else if (sortBy.equals("Downvotes")){
            return posts.stream().sorted(Comparator.comparingInt(Post::getDownvote).reversed()).toList();
        } else if(sortBy.equals("Replies")){
            posts = posts.stream()
                    .sorted(Comparator.comparingInt(Post::PostRepliesCount).reversed())
                    .collect(Collectors.toList());
        } else if(sortBy.equals("Repost")){
            posts = posts.stream()
                    .sorted(Comparator.comparingInt(Post::getRepost).reversed())
                    .collect(Collectors.toList());
        }

        return posts;
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

package ParentHiveApp.web.controller;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import ParentHiveApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
//    TODO
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/createpost/add")
    public String savePost(@RequestParam String Category,
                           @RequestParam String Title,
                           @RequestParam String Content
                           ){

        Long userId = userService.getCurrentUserId();
//        String title, String content, String category, User user
        this.postService.createPost(Title, Content, Category, userService.getUserById(userId));

        return "redirect:/profile";
    }

    @PostMapping("/posts/{postId}/upvote")
    public String upvote(@PathVariable Long postId,
                         HttpServletRequest request) {
        Long userId = userService.getCurrentUserId();
        postService.upvotePost(postId, userService.getUserById(userId));

        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/home"); // or wherever the user should be redirected
    }

    @PostMapping("/posts/{postId}/downvote")
    public String downvote(@PathVariable Long postId,
                           HttpServletRequest request) {
        Long userId = userService.getCurrentUserId();
        postService.downvotePost(postId, userService.getUserById(userId));
        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/home");
    }

    @PostMapping("/posts/{postId}/repost")
    public String repost(@PathVariable Long postId,
                         HttpServletRequest request) {
        Long userId = userService.getCurrentUserId();
        postService.repostPost(postId, userService.getUserById(userId));
        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/home");
    }

}

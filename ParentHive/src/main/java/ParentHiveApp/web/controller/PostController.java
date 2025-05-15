package ParentHiveApp.web.controller;

import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import ParentHiveApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}

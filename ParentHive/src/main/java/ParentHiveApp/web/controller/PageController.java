package ParentHiveApp.web.controller;

import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import ParentHiveApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class PageController {

    private final UserService userService;
    private final UserRepositoryJpa userRepositoryJpa;
    private final PostService postService;

    public PageController(UserService userService, UserRepositoryJpa userRepositoryJpa, PostService postService) {
        this.userService = userService;
        this.userRepositoryJpa = userRepositoryJpa;
        this.postService = postService;
    }

    @RequestMapping({ "/home", "/" })
    public String home(Model model) {
        //      list posts
        model.addAttribute("posts", postService.listPosts());
        return "home"; // will load resources/templates/home.html
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Optional<User> user = userRepositoryJpa.findById(userService.getCurrentUserId());
        model.addAttribute("username", user.get().getUsername());
//      list posts from user
        model.addAttribute("posts", user.get().getPosts());

        return "profile"; // will load resources/templates/profile.html
    }
    @GetMapping("/help")
    public String help() {
        return "help"; // will load resources/templates/help.html
    }

    @GetMapping("/createpost")
    public String createPost() {
        return "createpost"; // will load resources/templates/createpost.html
    }


}


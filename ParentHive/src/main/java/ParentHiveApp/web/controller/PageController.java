package ParentHiveApp.web.controller;

import ParentHiveApp.model.User;
import ParentHiveApp.service.PostService;
import ParentHiveApp.service.ReplyService;
import ParentHiveApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private final UserService userService;
    private final PostService postService;
    private final ReplyService replyService;

    public PageController(UserService userService, PostService postService, ReplyService replyService) {
        this.userService = userService;
        this.postService = postService;
        this.replyService = replyService;
    }

    @RequestMapping({ "/home", "/" })
    public String home(Model model) {
        //      list posts
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        model.addAttribute("posts", postService.listPosts());
        return "home"; // will load resources/templates/home.html
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        model.addAttribute("username", user.get().getUsername());
//      list posts from user
        model.addAttribute("posts", user.get().getPosts());

        return "profile"; // will load resources/templates/profile.html
    }
    @GetMapping("/help")
    public String help(Model model) {
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        return "help"; // will load resources/templates/help.html
    }

    @GetMapping("/createpost")
    public String createPost(Model model) {
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        return "createpost"; // will load resources/templates/createpost.html
    }
//    Your questions - Display user posts
//
//
//Your echoes - Display user replies
//
//Your bee-lieves - Display user liked posts
//
//Your reposts - Display user reposted posts
    @GetMapping({ "/questions"})
    public String questions(Model model) {
        //      list posts
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        model.addAttribute("posts", user.get().getPosts());

        return "displayUserInfoSideBar"; // will load resources/templates/home.html
    }

    @GetMapping({ "/echoes"})
    public String echoes(Model model) {
        //      list posts
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        model.addAttribute("replies", replyService.listReplies().stream()
                .filter(i -> i.getUser().getId() == user.get().getId())
                .collect(Collectors.toList()));

        return "displayUserInfoComments"; // will load resources/templates/home.html
    }

    @GetMapping({ "/beliefs"})
    public String beliefs(Model model) {
        //      list posts
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        model.addAttribute("posts", postService.listPosts().stream()
                .filter(i -> user.map(u -> u.getUpVotedPosts().contains(i.getId())).orElse(false))
                .collect(Collectors.toList()));

        return "displayUserInfoSideBar"; // will load resources/templates/home.html
    }

    @GetMapping({ "/reposts"})
    public String reposts(Model model) {
        //      list posts
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        model.addAttribute("posts", postService.listPosts().stream()
                .filter(i -> user.map(u -> u.getRepostedPosts().contains(i.getId())).orElse(false))
                .collect(Collectors.toList()));

        return "displayUserInfoSideBar"; // will load resources/templates/home.html
    }


}


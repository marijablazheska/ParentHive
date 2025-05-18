package ParentHiveApp.web.controller;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.User;
import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import ParentHiveApp.service.PostService;
import ParentHiveApp.service.ReplyService;
import ParentHiveApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
//    TODO
    private final PostService postService;
    private final UserService userService;
    private final ReplyService replyService;

    public PostController(PostService postService, UserService userService, ReplyService replyService) {
        this.postService = postService;
        this.userService = userService;
        this.replyService = replyService;
    }
    // search
    @GetMapping("/posts")
    public String getPostsPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String category,
                               Model model){
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        if (search != null && !category.equals("")) {
            model.addAttribute("posts", postService.getPostByTitleAndCategory(search, category));
        } else if(search != null && category.isEmpty()){
            model.addAttribute("posts", postService.getPostByTitle(search));
        } else if(search == null && !category.isEmpty()){
            model.addAttribute("posts", postService.getPostByCategory(category));
        }else {
            model.addAttribute("posts", postService.listPosts());
        }

        return "home";
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
//  Get report page
    @GetMapping("/posts/{postId}/report")
    public String reportPostPage(@PathVariable Long postId, Model model){
        Long userId = userService.getCurrentUserId();
        model.addAttribute("postId", postId);

        return "reportPost.html";
    }

    //  Send report page
    @PostMapping ("/posts/report/{postId}")
    public String reportPost(@PathVariable Long postId){
        Long userId = userService.getCurrentUserId();
//      TODO Implement functionality
        return "redirect:/posts/" + postId;
    }

//    View post
    @GetMapping("/posts/{postId}")
    public String post(@PathVariable Long postId, Model model) {

        model.addAttribute("post", postService.getPostById(postId));
        model.addAttribute("replies", postService.getPostById(postId).getReplies());

        return "viewpost.html"; // will load resources/templates/home.html
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

    //    addReply form
    @GetMapping("/posts/addReply/{postId}")
    public String addReply(@PathVariable Long postId, Model model) {
//        Post post = postService.getPostById(postId);
        model.addAttribute("postId", postId);

        return "createReply.html"; // will load resources/templates/home.html
    }

    @PostMapping("/posts/addReply/{postId}")
    public String saveReply(@RequestParam String Content,
                           @PathVariable Long postId, Model model

    ){
        Long userId = userService.getCurrentUserId();
        model.addAttribute("postId", postId);
        this.replyService.createReply(Content, userService.getUserById(userId), postService.getPostById(postId));

        return "redirect:/posts/" + postId;
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

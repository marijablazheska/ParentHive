package ParentHiveApp.web.controller;

import ParentHiveApp.model.Post;
import ParentHiveApp.model.Reply;
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
import java.util.Optional;

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
                               @RequestParam(required = false) String sortby,
                               Model model){
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }


        if (search != null && !category.equals("")) {
            if(!sortby.equals("")){
                model.addAttribute("posts", postService.sortBy(sortby, postService.getPostByTitleAndCategory(search, category)));
            } else {
                model.addAttribute("posts", postService.getPostByTitleAndCategory(search, category));
            }

        } else if(search != null && category.isEmpty()){
            if(!sortby.equals("")){
                model.addAttribute("posts", postService.sortBy(sortby, postService.getPostByTitle(search)));
            } else {
                model.addAttribute("posts", postService.getPostByTitle(search));
            }
        } else if(search == null && !category.isEmpty()){
            if(!sortby.equals("")){
                model.addAttribute("posts", postService.sortBy(sortby, postService.getPostByCategory(category)));
            } else {
                model.addAttribute("posts", postService.getPostByCategory(category));
            }

        }else {
            if(!sortby.equals("")){
                model.addAttribute("posts", postService.sortBy(sortby, postService.listPosts()));
            } else {
                model.addAttribute("posts", postService.listPosts());
            }
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
    //  Get edit page
    @GetMapping("/editPost/{postId}")
    public String getEditPost(Model model, @PathVariable Long postId) {
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);

        return "editPost"; // will load resources/templates/createpost.html
    }
    //  Not cooked
    @PostMapping("/editPost/{postId}")
    public String editPost(@RequestParam String Category,
                           @RequestParam String Title,
                           @RequestParam String Content,
                           @PathVariable Long postId
    ){

        Long userId = userService.getCurrentUserId();
//      Long id, String title, String content, String category
        Optional<User> user = userService.getUserById(userId);
        Post post = postService.getPostById(postId);
        if(user.isPresent() && user.get().getPosts().contains(post)){
            this.postService.updatePost(postId, Title, Content, Category);
        } else {
            return "redirect:/posts/" + postId + "?error=failedToEditPost";
        }

        return "redirect:/posts/" + postId;
    }
//  Get report page
    @GetMapping("/posts/{postId}/report")
    public String reportPostPage(@PathVariable Long postId, Model model){
        Long userId = userService.getCurrentUserId();
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));
        model.addAttribute("postId", postId);

        return "reportPost.html";
    }

    //  Send report page
    @PostMapping ("/posts/report/{postId}")
    public String reportPost(@PathVariable Long postId){
        Long userId = userService.getCurrentUserId();
//      TODO Implement functionality
        return "redirect:/posts/" + postId + "?report=success";
    }

//    View post
    @GetMapping("/posts/{postId}")
    public String post(@PathVariable Long postId, @RequestParam(required = false) String report, Model model) {
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        Post post = postService.getPostById(postId);
        post.setContent(post.getContent().replaceAll(
                "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)",
                "<a href=\"$1\" target=\"_blank\">$1</a>"
        ));

        model.addAttribute("post", post);
        model.addAttribute("replies", postService.getPostById(postId).getReplies());

        // Add report message to model if present
        if (report != null && !report.isEmpty()) {
            if(report.equals("success")){
                model.addAttribute("reportSuccess", true);
                model.addAttribute("reportMessage", "Your report has been submitted successfully!");
            } else {
                model.addAttribute("reportSuccess", false);
                model.addAttribute("reportMessage", "Your report has not been submitted error encountered, please retry!");
            }

        }

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

    //    editReply form
    @GetMapping("/replies/edit/{replyId}")
    public String editReply(@PathVariable Long replyId, Model model) {
//        Post post = postService.getPostById(postId);
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        model.addAttribute("reply", replyService.findById(replyId));

        return "editReply.html"; // will load resources/templates/home.html
    }

    @PostMapping("/replies/edit/{replyId}")
    public String editReply(@RequestParam String Content,
                            @PathVariable Long replyId, Model model

    ){
        Optional<User> user = userService.getUserById(userService.getCurrentUserId());
        user.ifPresent(userBap -> model.addAttribute("user", userBap));

        Reply reply = replyService.findById(replyId);
        model.addAttribute("reply", reply);
        if(reply.getUser().getId() == user.get().getId()){
            this.replyService.updateReply(replyId, Content);
        } else { return "redirect:/echoes?error=invalidRequest"; }


        return "redirect:/posts/" + reply.getPost().getId();
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

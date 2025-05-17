package ParentHiveApp.web.controller;

import ParentHiveApp.service.ReplyService;
import ParentHiveApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReplyController {
//    TODO

    private final UserService userService;
    private final ReplyService replyService;

    public ReplyController(UserService userService, ReplyService replyService) {
        this.userService = userService;
        this.replyService = replyService;
    }

    @PostMapping("/replies/{postId}/upvote")
    public String upvote(@PathVariable Long postId,
                         HttpServletRequest request) {
        Long userId = userService.getCurrentUserId();
        replyService.upvoteReply(postId, userService.getUserById(userId));

        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/home"); // or wherever the user should be redirected
    }

    @PostMapping("/replies/{postId}/downvote")
    public String downvote(@PathVariable Long postId,
                           HttpServletRequest request) {
        Long userId = userService.getCurrentUserId();
        replyService.downvoteReply(postId, userService.getUserById(userId));
        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/home");
    }
}

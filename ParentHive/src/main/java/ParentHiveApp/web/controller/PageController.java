package ParentHiveApp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/home")
    public String home() {
        return "home"; // will load resources/templates/home.html
    }

    @GetMapping("/profile")
    public String profile() {
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

    @GetMapping("/post")
    public String post() {
        return "viewpost"; // will load resources/templates/home.html
    }
}


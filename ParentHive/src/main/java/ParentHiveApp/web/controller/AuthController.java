package ParentHiveApp.web.controller;

import ParentHiveApp.dto.UserRegistrationDto;
import ParentHiveApp.model.Role;
import ParentHiveApp.model.User;
import ParentHiveApp.service.UserService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import ParentHiveApp.repository.jpa.UserRepositoryJpa;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepositoryJpa userRepositoryJpa;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // maps to login.html in templates folder
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new UserRegistrationDto());
        model.addAttribute("showVerificationModal", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registrationForm") @Valid UserRegistrationDto userDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        // Check if username or email already exists
        if (userService.existsByUsername(userDto.getUsername())) {
            result.rejectValue("username", null, "Username is already taken");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            result.rejectValue("email", null, "Email is already registered");
        }

        if (result.hasErrors()) {
            return "register"; // return back to the form with validation errors
        }

        // Map DTO to Entity
        userService.registerUser(userDto);

        // Redirect based on type
        if(userDto.getSelectedRole().equals(Role.PROFESSIONAL)){
            model.addAttribute("showVerificationModal", true); // signal Thymeleaf to show modal
            return "register"; // return the register page with modal visible
        }

        model.addAttribute("showParentSuccessModal", true);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You may now log in.");
        return "redirect:/login";
    }
}


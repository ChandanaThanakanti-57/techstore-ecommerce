package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.techstore.ecommercemaven.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String updated) {
        System.out.println(
                session.getAttribute("loggedInUser"));

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        if (updated != null) {
            model.addAttribute(
                    "success",
                    "Profile updated successfully!");
        }

        return "profile";
    }
    private final UserService userService;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit-profile")
    public String editProfile(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "edit-profile";


    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String password,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        user.setName(name);

        if (!password.isBlank()) {
            user.setPassword(
                    encoder.encode(password));
        }

        userService.updateUser(user);

        session.setAttribute(
                "loggedInUser",
                user);

        return "redirect:/profile?updated=true";

    }

}

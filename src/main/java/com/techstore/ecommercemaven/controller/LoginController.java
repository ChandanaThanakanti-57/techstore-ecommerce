package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserService userService;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // REGISTER
    // =========================

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {

            model.addAttribute(
                    "error",
                    "Passwords do not match");

            return "register";
        }

        if (userService.findByEmail(email) != null) {

            model.addAttribute(
                    "error",
                    "Email already exists");

            return "register";
        }

        User user = new User();

        user.setName(name);
        user.setEmail(email);

        // Encrypt password
        user.setPassword(password);

        // Default role
        user.setRole("USER");

        userService.saveUser(user);

        model.addAttribute(
                "success",
                "Registration Successful! Please Login.");

        return "login";
    }

    // =========================
    // LOGIN
    // =========================

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user =
                userService.findByEmail(email);

        System.out.println("USER = " + user);

        if (user != null) {

            System.out.println("DB PASSWORD = " + user.getPassword());

            boolean match =
                    encoder.matches(
                            password,
                            user.getPassword());

            System.out.println("MATCH = " + match);
        }

        if (user != null &&
                encoder.matches(
                        password,
                        user.getPassword())) {

            session.setAttribute(
                    "loggedInUser",
                    user);

            return "redirect:/products";
        }

        model.addAttribute(
                "error",
                "Invalid Email or Password");

        return "login";
    }

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Login Controller Works";
    }
}


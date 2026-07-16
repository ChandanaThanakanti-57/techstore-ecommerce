package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.PasswordResetToken;
import com.techstore.ecommercemaven.service.ForgotPasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(
            ForgotPasswordService forgotPasswordService) {

        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendResetLink(
            @RequestParam String email,
            Model model) {

        forgotPasswordService.sendResetLink(email);

        model.addAttribute(
                "success",
                "If the email exists, a password reset link has been sent.");

        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam String token,
            Model model) {

        PasswordResetToken resetToken =
                forgotPasswordService.validateToken(token);

        if (resetToken == null) {

            model.addAttribute(
                    "error",
                    "Invalid or expired reset link.");

            return "login";
        }

        model.addAttribute("token", token);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {

            model.addAttribute(
                    "error",
                    "Passwords do not match.");

            model.addAttribute("token", token);

            return "reset-password";
        }

        PasswordResetToken resetToken =
                forgotPasswordService.validateToken(token);

        if (resetToken == null) {

            model.addAttribute(
                    "error",
                    "Invalid or expired reset link.");

            return "login";
        }

        forgotPasswordService.updatePassword(
                resetToken,
                password);

        model.addAttribute(
                "success",
                "Password updated successfully. Please log in.");

        return "login";
    }
}
package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.PasswordResetToken;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.repository.PasswordResetTokenRepository;
import com.techstore.ecommercemaven.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public ForgotPasswordService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public void sendResetLink(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                new PasswordResetToken(
                        token,
                        user,
                        LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(resetToken);

        String link =
                "http://localhost:8080/reset-password?token=" + token;

        emailService.sendSimpleEmail(
                email,
                "Password Reset",
                "Click the link below to reset your password:\n\n"
                        + link
                        + "\n\nThis link expires in 30 minutes.");
    }

    public PasswordResetToken validateToken(String token) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null) {
            return null;
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return null;
        }

        return resetToken;
    }

    public void updatePassword(
            PasswordResetToken token,
            String newPassword) {

        User user = token.getUser();

        user.setPassword(
                encoder.encode(newPassword));

        userRepository.save(user);

        tokenRepository.delete(token);
    }
}
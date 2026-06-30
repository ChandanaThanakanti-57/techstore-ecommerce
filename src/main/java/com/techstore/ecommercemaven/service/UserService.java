package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {

        user.setPassword(
                encoder.encode(user.getPassword()));

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }

}

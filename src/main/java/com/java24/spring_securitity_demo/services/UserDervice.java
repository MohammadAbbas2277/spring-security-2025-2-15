package com.java24.spring_securitity_demo.services;

import com.java24.spring_securitity_demo.models.Role;
import com.java24.spring_securitity_demo.models.User;
import com.java24.spring_securitity_demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDervice {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDervice(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // register user
    public void RegisterUser(User user) {
        // hash password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ensure the user has at least defailt rolw user
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }

        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    // chexk if username already exists.
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }




}

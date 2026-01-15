package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.ApiException;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .designation(user.getDesignation() != null ? user.getDesignation().getTitle() : null)
                .organization(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .build();
    }

    @GetMapping("/findUser")
    public UserResponse findUser(@RequestParam String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping("/findOrgDetails")
    public String findOrgDetails(@RequestParam String email) {
        return userService.getOrgDetailsforEmployee(email);
    }
}

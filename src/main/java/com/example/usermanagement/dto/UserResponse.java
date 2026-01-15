package com.example.usermanagement.dto;

import com.example.usermanagement.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String designation;
    private String organization;
}

public UserResponse(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.fullName = user.getFullName();
    this.designation = user.getDesignation().getTitle();
    this.organization = user.getOrganization().getName();
}

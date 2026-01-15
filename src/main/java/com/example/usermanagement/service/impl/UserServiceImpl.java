package com.example.usermanagement.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;

@Service

public class UserServiceImpl {
    
    @Autowired
    private UserRepository userRepository;

    public UserResponse findUserByEmail(String email) {
        UserResponse response = new UserResponse();
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isPresent()) {
                response = new UserResponse(user.get());
            }
        } catch (Exception e) {
            System.out.println("Error occured while fetching user details");
        }
        return response;
    }

    public String getOrgDetailsforEmployee(String username) {
        String orgName = "";
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if(user.isPresent()) {
                User employee = user.get();
                orgName = employee.getOrganization().getDescription();
            }
        } catch (Exception e) {
            System.out.println("Error occured while fetching user details");
        }
        return orgName;
    }

}

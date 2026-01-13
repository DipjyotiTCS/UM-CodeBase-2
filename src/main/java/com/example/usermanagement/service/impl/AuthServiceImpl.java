package com.example.usermanagement.service.impl;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.entity.*;
import com.example.usermanagement.exception.ApiException;
import com.example.usermanagement.repository.DesignationRepository;
import com.example.usermanagement.repository.OrganizationRepository;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.JwtService;
import com.example.usermanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DesignationRepository designationRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           DesignationRepository designationRepository,
                           OrganizationRepository organizationRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.designationRepository = designationRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
        }

        Designation designation = designationRepository.findByTitle(request.getDesignationTitle())
                .orElseGet(() -> {
                    Designation d = new Designation();
                    d.setTitle(request.getDesignationTitle());
                    return designationRepository.save(d);
                });

        Organization organization = organizationRepository.findByName(request.getOrganizationName())
                .orElseGet(() -> {
                    Organization o = new Organization();
                    o.setName(request.getOrganizationName());
                    return organizationRepository.save(o);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDesignation(designation);
        user.setOrganization(organization);

        if (request.getAddress() != null) {
            Address a = new Address();
            a.setLine1(request.getAddress().getLine1());
            a.setLine2(request.getAddress().getLine2());
            a.setCity(request.getAddress().getCity());
            a.setState(request.getAddress().getState());
            a.setPostalCode(request.getAddress().getPostalCode());
            a.setCountry(request.getAddress().getCountry());
            user.setAddress(a);
        }

        User saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsernameOrEmail(), request.getPassword()
            ));
        } catch (AuthenticationException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String subject = userRepository.findByUsername(request.getUsernameOrEmail())
                .map(User::getUsername)
                .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                        .map(User::getUsername)
                        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials")));

        String token = jwtService.generateAccessToken(subject);
        return new AuthResponse(token, "Bearer");
    }

    @Override
    public TokenValidationResponse validate(String bearerTokenOrRawToken) {
        if (bearerTokenOrRawToken == null || bearerTokenOrRawToken.trim().isEmpty()) {
            return new TokenValidationResponse(false, null);
        }
        String token = bearerTokenOrRawToken.startsWith("Bearer ")
                ? bearerTokenOrRawToken.substring(7)
                : bearerTokenOrRawToken;

        boolean valid = jwtService.isValid(token);
        return new TokenValidationResponse(valid, valid ? jwtService.getSubject(token) : null);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .designation(user.getDesignation() != null ? user.getDesignation().getTitle() : null)
                .organization(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .build();
    }
}

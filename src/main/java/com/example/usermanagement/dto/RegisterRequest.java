package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotBlank @Size(min = 3, max = 80)
    private String username;

    @NotBlank @Email @Size(max = 200)
    private String email;

    @NotBlank @Size(min = 8, max = 120)
    private String password;

    @NotBlank @Size(max = 120)
    private String fullName;

    @NotBlank @Size(max = 120)
    private String designationTitle;

    @NotBlank @Size(max = 150)
    private String organizationName;

    @Valid
    private AddressDto address;
}

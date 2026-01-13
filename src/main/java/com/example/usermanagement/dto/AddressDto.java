package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddressDto {

    @NotBlank @Size(max = 200)
    private String line1;

    @Size(max = 200)
    private String line2;

    @NotBlank @Size(max = 100)
    private String city;

    @NotBlank @Size(max = 100)
    private String state;

    @NotBlank @Size(max = 20)
    private String postalCode;

    @NotBlank @Size(max = 100)
    private String country;
}

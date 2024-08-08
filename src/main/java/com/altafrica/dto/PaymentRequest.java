package com.altafrica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotEmpty
    private String domain;

    @Email
    @NotEmpty
    private String email;

    private int amount; // amount in kobo

    // Getters and setters
    // ...
}


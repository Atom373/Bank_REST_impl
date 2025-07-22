package com.example.bankcards.controller.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
		@Email(message = "Must be in email address format")
        @NotBlank(message = "Email can not be blank")
        String email,

        @NotBlank(message = "Password can not be blank")
        @Size(min = 5, max = 10, message = "Password must be between 5 and 10 characters")
        String password
) {}
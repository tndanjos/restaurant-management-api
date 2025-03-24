package br.com.fiap.techchallenge.restaurantmanagementapi.dto.request;

import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "The email cannot be blank")
        @Size(min = 5, max = 100, message = "The username must be between 5 and 100 characters")
        @Email(message = "The email must be valid")
        String email,

        @NotNull(message = "The type cannot be null")
        UserType type,

        CreateAddressRequestDto address,

        @NotBlank(message = "The username cannot be blank")
        @Size(min = 4, max = 50, message = "The username must be between 5 and 50 characters")
        String username
) { }
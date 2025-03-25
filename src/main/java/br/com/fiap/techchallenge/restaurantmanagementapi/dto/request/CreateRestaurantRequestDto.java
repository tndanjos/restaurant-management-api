package br.com.fiap.techchallenge.restaurantmanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public record CreateRestaurantRequestDto(
        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
        String name,

        String cooking,
        CreateAddressRequestDto address,
        LocalTime openingAt,

        @NotNull(message = "User ID must be provided")
        Long userId
) { }
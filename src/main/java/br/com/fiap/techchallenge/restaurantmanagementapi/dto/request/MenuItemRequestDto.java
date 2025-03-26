package br.com.fiap.techchallenge.restaurantmanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequestDto (
        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
        String name,

        String description,
        BigDecimal price,
        String photoUrl,
        Boolean dineIn,

        @NotNull(message = "Restaurant ID must be provided")
        Long restaurantId
){ }

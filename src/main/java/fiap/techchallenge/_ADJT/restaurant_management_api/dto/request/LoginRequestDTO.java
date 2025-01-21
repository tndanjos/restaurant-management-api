package fiap.techchallenge._ADJT.restaurant_management_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) { }
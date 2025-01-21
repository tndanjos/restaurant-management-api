package fiap.techchallenge._ADJT.user_management_api.dto.response;

import fiap.techchallenge._ADJT.user_management_api.entity.User;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String username,
        LocalDateTime updatedAt) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getUpdatedAt()
        );

    }
}

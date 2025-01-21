package fiap.techchallenge._ADJT.restaurant_management_api.dto.response;

import fiap.techchallenge._ADJT.restaurant_management_api.dto.AddressDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.entity.User;
import fiap.techchallenge._ADJT.restaurant_management_api.enums.UserType;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String username,
        UserType type,
        AddressDTO address,
        String updatedAt) {
    public static UserResponseDTO fromEntity(User user) {
        AddressDTO addressDTO = new AddressDTO(
                user.getAddress().getStreet(),
                user.getAddress().getNeighborhood(),
                user.getAddress().getZipCode(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getNumber(),
                user.getAddress().getComplement()
        );

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getType(),
                addressDTO,
                user.getFormattedUpdatedAt()
        );
    }
}


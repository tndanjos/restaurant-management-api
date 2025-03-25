package br.com.fiap.techchallenge.restaurantmanagementapi.dto.response;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String username,
        UserType type,
        AddressRequestDto address,
        String updatedAt) {

    public static UserResponseDto fromEntity(User user) {

        AddressRequestDto address = new AddressRequestDto(
                user.getAddress().getStreet(),
                user.getAddress().getNeighborhood(),
                user.getAddress().getZipCode(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getNumber(),
                user.getAddress().getComplement()
        );

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getType(),
                address,
                user.getFormattedUpdatedAt()
        );
    }
}


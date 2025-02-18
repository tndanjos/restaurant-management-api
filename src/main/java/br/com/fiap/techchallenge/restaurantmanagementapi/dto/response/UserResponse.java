package br.com.fiap.techchallenge.restaurantmanagementapi.dto.response;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.Address;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;

public record UserResponse(
        Long id,
        String name,
        String email,
        String username,
        UserType type,
        Address address,
        String updatedAt) {
    public static UserResponse fromEntity(User user) {
        Address address = new Address(
                user.getAddress().getStreet(),
                user.getAddress().getNeighborhood(),
                user.getAddress().getZipCode(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getNumber(),
                user.getAddress().getComplement()
        );

        return new UserResponse(
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


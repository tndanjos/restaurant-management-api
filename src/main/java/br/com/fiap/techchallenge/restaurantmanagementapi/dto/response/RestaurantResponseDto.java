package br.com.fiap.techchallenge.restaurantmanagementapi.dto.response;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record RestaurantResponseDto(
        Long id,
        String name,
        String cooking,
        AddressRequestDto address,

        @JsonFormat(pattern = "HH:mm")
        LocalTime openingAt,

        UserResponseDto owner
) {

    public static RestaurantResponseDto fromEntity(Restaurant restaurant) {
        AddressRequestDto address = new AddressRequestDto(
                restaurant.getAddress().getStreet(),
                restaurant.getAddress().getNeighborhood(),
                restaurant.getAddress().getZipCode(),
                restaurant.getAddress().getCity(),
                restaurant.getAddress().getState(),
                restaurant.getAddress().getNumber(),
                restaurant.getAddress().getComplement()
        );

        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCooking(),
                address,
                restaurant.getOpeningAt(),
                UserResponseDto.fromEntity(restaurant.getUser())
        );
    }
}

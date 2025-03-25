package br.com.fiap.techchallenge.restaurantmanagementapi.dto.response;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateAddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record RestaurantResponseDto(
        Long id,
        String name,
        String cooking,
        CreateAddressRequestDto address,

        @JsonFormat(pattern = "HH:mm")
        LocalTime openingAt,

        Long user_id
) {

    public static RestaurantResponseDto fromEntity(Restaurant restaurant) {
        CreateAddressRequestDto address = new CreateAddressRequestDto(
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
                restaurant.getUser().getId()
        );
    }
}

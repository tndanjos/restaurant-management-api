package br.com.fiap.techchallenge.restaurantmanagementapi.dto.request;

public record CreateAddressRequestDto(
        String street,
        String neighborhood,
        String zipCode,
        String city,
        String state,
        String number,
        String complement
) {
}

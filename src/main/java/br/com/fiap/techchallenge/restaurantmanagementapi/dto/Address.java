package br.com.fiap.techchallenge.restaurantmanagementapi.dto;

public record Address(
        String street,
        String neighborhood,
        String zipCode,
        String city,
        String state,
        String number,
        String complement
) {
}

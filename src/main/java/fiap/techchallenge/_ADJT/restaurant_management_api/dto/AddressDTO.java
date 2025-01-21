package fiap.techchallenge._ADJT.restaurant_management_api.dto;

public record AddressDTO(
        String street,
        String neighborhood,
        String zipCode,
        String city,
        String state,
        String number,
        String complement
) {
}

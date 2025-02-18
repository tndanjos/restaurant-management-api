package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String street;
    private String neighborhood;
    private String zipCode;
    private String city;
    private String state;
    private String number;
    private String complement;

    public Address(br.com.fiap.techchallenge.restaurantmanagementapi.dto.Address dto) {
        this.street = dto.street();
        this.neighborhood = dto.neighborhood();
        this.zipCode = dto.zipCode();
        this.city = dto.city();
        this.state = dto.state();
        this.number = dto.number();
        this.complement = dto.complement();
    }
}
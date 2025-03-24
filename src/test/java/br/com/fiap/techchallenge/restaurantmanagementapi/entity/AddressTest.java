package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateAddressRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest {

    private Address address;

    @BeforeEach
    void setup() {
        CreateAddressRequestDto dto = new CreateAddressRequestDto(
            "Rua Augusta",
            "Consolação",
            "01310-100",
            "São Paulo",
            "SP",
            "123",
            "Fundos"
        );

        address = new Address(dto);
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        assertThat(address.getStreet()).isEqualTo("Rua Augusta");
        assertThat(address.getNeighborhood()).isEqualTo("Consolação");
        assertThat(address.getZipCode()).isEqualTo("01310-100");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getComplement()).isEqualTo("Fundos");
    }
}

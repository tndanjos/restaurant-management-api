package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RestaurantTest {

    private Restaurant restaurant;
    private User owner;

    @BeforeEach
    void setup() {
        AddressRequestDto addressDto = new AddressRequestDto(
                "Av. Paulista",
                "Bela Vista",
                "01311-000",
                "São Paulo",
                "SP",
                "1578",
                "5º andar"
        );

        RestaurantRequestDto dto = new RestaurantRequestDto(
                "Restaurante do João",
                "Brasileira",
                addressDto,
                LocalTime.of(10, 30),
                1L
        );

        CreateUserRequestDto userDto = new CreateUserRequestDto(
                "João Silva",
                "joao.silva@gmail.com",
                UserType.OWNER,
                addressDto,
                "joaosilva",
                "senhaSegura123"
        );

        owner = new User(userDto);
        owner.setId(1L);
        restaurant = new Restaurant(dto, owner);
    }

    @Test
    void shouldCreateRestaurantSuccessfullyFromDto() {
        assertThat(restaurant.getName()).isEqualTo("Restaurante do João");
        assertThat(restaurant.getCooking()).isEqualTo("Brasileira");

        assertThat(restaurant.getAddress()).isNotNull();
        assertThat(restaurant.getAddress().getStreet()).isEqualTo("Av. Paulista");

        assertThat(restaurant.getUser()).isNotNull();
        assertThat(restaurant.getUser().getName()).isEqualTo("João Silva");

        assertThat(restaurant.getOpeningAt()).isEqualTo(LocalTime.of(10, 30));
        assertThat(restaurant.getCreatedAt()).isNotNull();
        assertThat(restaurant.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldSetAndRetrieveTimestampsCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 12, 25, 9, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 12, 26, 18, 30, 0);

        restaurant.setCreatedAt(createdAt);
        restaurant.setUpdatedAt(updatedAt);

        assertThat(restaurant.getCreatedAt()).isEqualTo(createdAt);
        assertThat(restaurant.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        CreateUserRequestDto newUserDto = new CreateUserRequestDto(
                "Maria Oliveira",
                "maria.oliveira@gmail.com",
                UserType.OWNER,
                new AddressRequestDto(
                        "Rua das Flores",
                        "Centro",
                        "01001-000",
                        "Rio de Janeiro",
                        "RJ",
                        "500",
                        "Apto 101"
                ),
                "mariaoliveira",
                "outrasenha123"
        );

        User newUser = new User(newUserDto);
        restaurant.setUser(newUser);

        assertThat(restaurant.getUser().getName()).isEqualTo("Maria Oliveira");
        assertThat(restaurant.getUser().getUsername()).isEqualTo("mariaoliveira");
    }
}

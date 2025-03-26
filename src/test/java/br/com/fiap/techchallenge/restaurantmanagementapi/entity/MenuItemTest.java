package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class MenuItemTest {

    private Restaurant restaurant;
    private MenuItem menuItem;

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

        CreateUserRequestDto userDto = new CreateUserRequestDto(
                "João Silva",
                "joao.silva@gmail.com",
                UserType.OWNER,
                addressDto,
                "joaosilva",
                "senhaSegura123"
        );

        User owner = new User(userDto);
        owner.setId(1L);

        RestaurantRequestDto restaurantDto = new RestaurantRequestDto(
                "Restaurante do João",
                "Brasileira",
                addressDto,
                LocalTime.of(10, 30),
                1L
        );

        restaurant = new Restaurant(restaurantDto, owner);
        restaurant.setId(1L);

        MenuItemRequestDto menuItemDto = new MenuItemRequestDto(
                "Feijoada Completa",
                "Feijoada com acompanhamentos tradicionais",
                BigDecimal.valueOf(49.90),
                "https://example.com/feijoada.jpg",
                true,
                restaurant.getId()
        );

        menuItem = new MenuItem(menuItemDto, restaurant);
    }

    @Test
    void shouldCreateMenuItemSuccessfullyFromDto() {
        assertThat(menuItem.getName()).isEqualTo("Feijoada Completa");
        assertThat(menuItem.getDescription()).isEqualTo("Feijoada com acompanhamentos tradicionais");
        assertThat(menuItem.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(49.90));
        assertThat(menuItem.getPhotoUrl()).isEqualTo("https://example.com/feijoada.jpg");
        assertThat(menuItem.getDineIn()).isTrue();

        assertThat(menuItem.getRestaurant()).isNotNull();
        assertThat(menuItem.getRestaurant().getName()).isEqualTo("Restaurante do João");

        assertThat(menuItem.getCreatedAt()).isNotNull();
        assertThat(menuItem.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldSetAndRetrieveTimestampsCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 12, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 12, 2, 14, 30);

        menuItem.setCreatedAt(createdAt);
        menuItem.setUpdatedAt(updatedAt);

        assertThat(menuItem.getCreatedAt()).isEqualTo(createdAt);
        assertThat(menuItem.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {
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

        User newOwner = new User(newUserDto);
        newOwner.setId(2L);

        Restaurant newRestaurant = new Restaurant(
                new RestaurantRequestDto(
                        "Restaurante da Maria",
                        "Italiana",
                        newUserDto.address(),
                        LocalTime.of(11, 0),
                        newOwner.getId()
                ),
                newOwner
        );

        newRestaurant.setId(2L);
        menuItem.setRestaurant(newRestaurant);

        assertThat(menuItem.getRestaurant().getName()).isEqualTo("Restaurante da Maria");
        assertThat(menuItem.getRestaurant().getCooking()).isEqualTo("Italiana");
    }
}

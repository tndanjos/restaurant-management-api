package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.RestaurantRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageService messageService;

    private RestaurantRequestDto dto;
    private User owner;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        AddressRequestDto addressDto = new AddressRequestDto(
                "Av. Paulista",
                "Bela Vista",
                "01311-000",
                "São Paulo",
                "SP",
                "1578",
                "5º andar"
        );

        dto = new RestaurantRequestDto(
                "Restaurante do João",
                "Brasileira",
                addressDto,
                LocalTime.of(10, 30),
                1L
        );

        owner = new User();
        owner.setId(1L);
        owner.setType(UserType.OWNER);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = restaurantService.createRestaurant(dto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Restaurante do João");
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageService.getMessage("user.not.found", 1L)).thenReturn("Usuário não encontrado");

        assertThatThrownBy(() -> restaurantService.createRestaurant(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void shouldThrowExceptionWhenUserIsCustomer() {
        owner.setType(UserType.CUSTOMER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(messageService.getMessage("user.not.allowed")).thenReturn("Usuário não tem permissão");

        assertThatThrownBy(() -> restaurantService.createRestaurant(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não tem permissão");
    }

    @Test
    void shouldGetRestaurantById() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(10L);
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFoundById() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        when(messageService.getMessage("restaurant.not.found", 99L)).thenReturn("Restaurante não encontrado");

        assertThatThrownBy(() -> restaurantService.getRestaurantById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurante não encontrado");
    }

    @Test
    void shouldReturnPaginatedRestaurants() {
        Restaurant restaurant = new Restaurant();
        Page<Restaurant> page = new PageImpl<>(List.of(restaurant));

        when(restaurantRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Restaurant> result = restaurantService.getAllRestaurants(0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        Restaurant existing = new Restaurant();
        existing.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = restaurantService.updateRestaurant(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Restaurante do João");
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        doNothing().when(restaurantRepository).delete(restaurant);

        restaurantService.deleteRestaurantById(1L);

        verify(restaurantRepository).delete(restaurant);
    }
}

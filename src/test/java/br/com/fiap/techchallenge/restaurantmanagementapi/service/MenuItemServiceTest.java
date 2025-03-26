package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.MenuItemRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuItemServiceTest {

    @InjectMocks
    private MenuItemService menuItemService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MessageService messageService;

    private MenuItemRequestDto dto;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante do João");

        dto = new MenuItemRequestDto(
                "Feijoada",
                "Feijoada completa com arroz, couve e laranja",
                BigDecimal.valueOf(49.90),
                "https://img.com/feijoada.jpg",
                true,
                restaurant.getId()
        );
    }

    @Test
    void shouldCreateMenuItemSuccessfully() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = menuItemService.createMenuItem(dto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Feijoada");
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFoundOnCreate() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageService.getMessage("restaurant.not.found", 1L)).thenReturn("Restaurante não encontrado");

        assertThatThrownBy(() -> menuItemService.createMenuItem(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurante não encontrado");
    }

    @Test
    void shouldGetMenuItemByIdSuccessfully() {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(10L);

        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(menuItem));

        MenuItem result = menuItemService.getMenuItemById(10L);
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFoundById() {
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());
        when(messageService.getMessage("menuItem.not.found", 99L)).thenReturn("Item de menu não encontrado");

        assertThatThrownBy(() -> menuItemService.getMenuItemById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Item de menu não encontrado");
    }

    @Test
    void shouldReturnPaginatedMenuItems() {
        MenuItem item = new MenuItem();
        Page<MenuItem> page = new PageImpl<>(List.of(item));

        when(menuItemRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<MenuItem> result = menuItemService.getAllMenuItems(0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        MenuItem existing = new MenuItem();
        existing.setId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = menuItemService.updateMenuItem(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Feijoada");
        assertThat(result.getRestaurant().getId()).isEqualTo(1L);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        MenuItem item = new MenuItem();
        item.setId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(menuItemRepository).delete(item);

        menuItemService.deleteMenuItemById(1L);

        verify(menuItemRepository).delete(item);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentMenuItem() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageService.getMessage("menuItem.not.found", 1L)).thenReturn("Item de menu não encontrado");

        assertThatThrownBy(() -> menuItemService.deleteMenuItemById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Item de menu não encontrado");
    }
}

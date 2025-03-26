package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.MenuItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuItemService menuItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurant restaurant;
    private MenuItemRequestDto menuItemDto;

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
                "joao@gmail.com",
                UserType.OWNER,
                addressDto,
                "joaosilva",
                "password123"
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

        menuItemDto = new MenuItemRequestDto(
                "Feijoada",
                "Feijoada completa com arroz, couve e laranja",
                BigDecimal.valueOf(49.90),
                "https://img.com/feijoada.jpg",
                true,
                restaurant.getId()
        );
    }

    @Test
    void shouldCreateMenuItemSuccessfully() throws Exception {
        MenuItem menuItem = new MenuItem(menuItemDto, restaurant);
        menuItem.setId(1L);

        given(menuItemService.createMenuItem(any(MenuItemRequestDto.class))).willReturn(menuItem);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Feijoada")))
                .andExpect(jsonPath("$.price", is(49.90)))
                .andExpect(jsonPath("$.dineIn", is(true)))
                .andExpect(jsonPath("$.restaurant.id", is(1)))
                .andExpect(jsonPath("$.restaurant.name", is("Restaurante do João")));
    }

    @Test
    void shouldGetMenuItemByIdSuccessfully() throws Exception {
        MenuItem menuItem = new MenuItem(menuItemDto, restaurant);
        menuItem.setId(1L);

        given(menuItemService.getMenuItemById(1L)).willReturn(menuItem);

        mockMvc.perform(get("/menu-items/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Feijoada")))
                .andExpect(jsonPath("$.price", is(49.90)))
                .andExpect(jsonPath("$.restaurant.name", is("Restaurante do João")));
    }

    @Test
    void shouldGetAllMenuItemsSuccessfully() throws Exception {
        MenuItem item1 = new MenuItem(menuItemDto, restaurant);
        item1.setId(1L);

        MenuItem item2 = new MenuItem(
                new MenuItemRequestDto(
                        "Lasanha",
                        "Lasanha à bolonhesa",
                        BigDecimal.valueOf(39.90),
                        "https://img.com/lasanha.jpg",
                        true,
                        restaurant.getId()
                ),
                restaurant
        );
        item2.setId(2L);

        Page<MenuItem> page = new PageImpl<>(List.of(item1, item2));

        given(menuItemService.getAllMenuItems(0, 10)).willReturn(page);

        mockMvc.perform(get("/menu-items?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() throws Exception {
        MenuItem updated = new MenuItem(menuItemDto, restaurant);
        updated.setId(1L);
        updated.setName("Feijoada Premium");

        given(menuItemService.updateMenuItem(eq(1L), any(MenuItemRequestDto.class))).willReturn(updated);

        mockMvc.perform(put("/menu-items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Feijoada Premium")));
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() throws Exception {
        doNothing().when(menuItemService).deleteMenuItemById(1L);

        mockMvc.perform(delete("/menu-items/{id}", 1))
                .andExpect(status().isNoContent());
    }
}

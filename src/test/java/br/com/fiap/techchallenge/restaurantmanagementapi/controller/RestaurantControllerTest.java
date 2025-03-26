package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.RestaurantService;
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
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantRequestDto dto;
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

        CreateUserRequestDto userDto = new CreateUserRequestDto(
                "João Silva",
                "joao@gmail.com",
                UserType.OWNER,
                addressDto,
                "joaosilva",
                "password123"
        );

        dto = new RestaurantRequestDto(
                "Restaurante do João",
                "Brasileira",
                addressDto,
                LocalTime.of(10, 30),
                1L
        );

        owner = new User(userDto);
        owner.setId(1L);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() throws Exception {
        Restaurant restaurant = new Restaurant(dto, owner);
        restaurant.setId(1L);

        given(restaurantService.createRestaurant(any(RestaurantRequestDto.class))).willReturn(restaurant);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurante do João")))
                .andExpect(jsonPath("$.cooking", is("Brasileira")))
                .andExpect(jsonPath("$.address.street", is("Av. Paulista")))
                .andExpect(jsonPath("$.address.neighborhood", is("Bela Vista")))
                .andExpect(jsonPath("$.address.zipCode", is("01311-000")))
                .andExpect(jsonPath("$.address.city", is("São Paulo")))
                .andExpect(jsonPath("$.address.state", is("SP")))
                .andExpect(jsonPath("$.address.number", is("1578")))
                .andExpect(jsonPath("$.address.complement", is("5º andar")))
                .andExpect(jsonPath("$.owner.id", is(1)))
                .andExpect(jsonPath("$.owner.name", is("João Silva")))
                .andExpect(jsonPath("$.owner.email", is("joao@gmail.com")))
                .andExpect(jsonPath("$.owner.username", is("joaosilva")))
                .andExpect(jsonPath("$.owner.type", is("OWNER")))
                .andExpect(jsonPath("$.owner.address.street", is("Av. Paulista")))
                .andExpect(jsonPath("$.owner.updatedAt").isNotEmpty());
    }

    @Test
    void shouldGetRestaurantByIdSuccessfully() throws Exception {
        Restaurant restaurant = new Restaurant(dto, owner);
        restaurant.setId(1L);

        given(restaurantService.getRestaurantById(1L)).willReturn(restaurant);

        mockMvc.perform(get("/restaurants/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurante do João")))
                .andExpect(jsonPath("$.cooking", is("Brasileira")))
                .andExpect(jsonPath("$.address.street", is("Av. Paulista")))
                .andExpect(jsonPath("$.address.neighborhood", is("Bela Vista")))
                .andExpect(jsonPath("$.address.zipCode", is("01311-000")))
                .andExpect(jsonPath("$.address.city", is("São Paulo")))
                .andExpect(jsonPath("$.address.state", is("SP")))
                .andExpect(jsonPath("$.address.number", is("1578")))
                .andExpect(jsonPath("$.address.complement", is("5º andar")))
                .andExpect(jsonPath("$.owner.id", is(1)))
                .andExpect(jsonPath("$.owner.name", is("João Silva")))
                .andExpect(jsonPath("$.owner.email", is("joao@gmail.com")))
                .andExpect(jsonPath("$.owner.username", is("joaosilva")))
                .andExpect(jsonPath("$.owner.type", is("OWNER")))
                .andExpect(jsonPath("$.owner.address.street", is("Av. Paulista")))
                .andExpect(jsonPath("$.owner.updatedAt").isNotEmpty());
    }

    @Test
    void shouldGetAllRestaurantsSuccessfully() throws Exception {
        Restaurant restaurant1 = new Restaurant(dto, owner);
        restaurant1.setId(1L);

        Restaurant restaurant2 = new Restaurant(
                new RestaurantRequestDto(
                        "Sushi Place", "Japonesa",
                        dto.address(), LocalTime.of(18, 0), 1L
                ),
                owner
        );
        restaurant2.setId(2L);

        Page<Restaurant> restaurantPage = new PageImpl<>(List.of(restaurant1, restaurant2));

        given(restaurantService.getAllRestaurants(0, 10)).willReturn(restaurantPage);

        mockMvc.perform(get("/restaurants?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() throws Exception {
        Restaurant updated = new Restaurant(dto, owner);
        updated.setId(1L);
        updated.setName("Atualizado");

        given(restaurantService.updateRestaurant(eq(1L), any(RestaurantRequestDto.class))).willReturn(updated);

        mockMvc.perform(put("/restaurants/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Atualizado")));
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() throws Exception {
        doNothing().when(restaurantService).deleteRestaurantById(1L);

        mockMvc.perform(delete("/restaurants/{id}", 1))
                .andExpect(status().isNoContent());
    }
}

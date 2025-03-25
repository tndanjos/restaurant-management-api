package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdatePasswordRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Address;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.UserService;
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
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private CreateUserRequestDto dto;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        dto = new CreateUserRequestDto(
                "João Silva",
                "joao.silva@gmail.com",
                UserType.OWNER,
                new AddressRequestDto(
                        "Rua Augusta",
                        "Consolação",
                        "01310-100",
                        "São Paulo",
                        "SP",
                        "123",
                        "Fundos"
                ),
                "joaosilva",
                "password123"
        );
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        User createdUser = new User(dto);
        createdUser.setId(1L);

        given(userService.createUser(any(CreateUserRequestDto.class))).willReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("João Silva")))
                .andExpect(jsonPath("$.email", is("joao.silva@gmail.com")))
                .andExpect(jsonPath("$.username", is("joaosilva")))
                .andExpect(jsonPath("$.type", is("OWNER")))
                .andExpect(jsonPath("$.address.street", is("Rua Augusta")))
                .andExpect(jsonPath("$.address.neighborhood", is("Consolação")))
                .andExpect(jsonPath("$.address.zipCode", is("01310-100")))
                .andExpect(jsonPath("$.address.city", is("São Paulo")))
                .andExpect(jsonPath("$.address.state", is("SP")))
                .andExpect(jsonPath("$.address.number", is("123")))
                .andExpect(jsonPath("$.address.complement", is("Fundos")))
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {
        User user = new User(dto);
        user.setId(1L);

        given(userService.getUserById(1L)).willReturn(user);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("João Silva")))
                .andExpect(jsonPath("$.email", is("joao.silva@gmail.com")))
                .andExpect(jsonPath("$.username", is("joaosilva")))
                .andExpect(jsonPath("$.type", is("OWNER")))
                .andExpect(jsonPath("$.address.street", is("Rua Augusta")))
                .andExpect(jsonPath("$.address.neighborhood", is("Consolação")))
                .andExpect(jsonPath("$.address.zipCode", is("01310-100")))
                .andExpect(jsonPath("$.address.city", is("São Paulo")))
                .andExpect(jsonPath("$.address.state", is("SP")))
                .andExpect(jsonPath("$.address.number", is("123")))
                .andExpect(jsonPath("$.address.complement", is("Fundos")))
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void shouldGetAllUsersSuccessfully() throws Exception {
        CreateUserRequestDto other_dto = new CreateUserRequestDto(
                "Maria Silva",
                "maria.silva@gmail.com",
                UserType.CUSTOMER,
                new AddressRequestDto(
                        "Avenida Paulista",
                        "Centro",
                        "01100-310",
                        "São Paulo",
                        "SP",
                        "321",
                        "Fundos"
                ),
                "mariasilva",
                "123password"
        );

        User user = new User(dto);
        user.setId(1L);

        User other_user = new User(other_dto);
        other_user.setId(2L);

        Page<User> usersPage = new PageImpl<>(List.of(user, other_user));

        given(userService.getAllUsers(0, 10)).willReturn(usersPage);

        mockMvc.perform(get("/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        UpdateUserRequestDto updateDto = new UpdateUserRequestDto(
                "test",
                "test@test.com",
                UserType.CUSTOMER,
                new AddressRequestDto(
                        "Rua Test",
                        "Test",
                        "00000-000",
                        "Test",
                        "TS",
                        "000",
                        "Test"
                ),
                "test"
        );

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName(updateDto.name());
        updatedUser.setEmail(updateDto.email());
        updatedUser.setType(updateDto.type());
        updatedUser.setAddress(new Address(updateDto.address()));
        updatedUser.setUsername(updateDto.username());
        updatedUser.setPassword("password123");


        given(userService.updateUser(eq(1L), any(UpdateUserRequestDto.class))).willReturn(updatedUser);

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.username", is("test")))
                .andExpect(jsonPath("$.type", is("CUSTOMER")))
                .andExpect(jsonPath("$.address.street", is("Rua Test")))
                .andExpect(jsonPath("$.address.neighborhood", is("Test")))
                .andExpect(jsonPath("$.address.zipCode", is("00000-000")))
                .andExpect(jsonPath("$.address.city", is("Test")))
                .andExpect(jsonPath("$.address.state", is("TS")))
                .andExpect(jsonPath("$.address.number", is("000")))
                .andExpect(jsonPath("$.address.complement", is("Test")));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdatePasswordSuccessfully() throws Exception {
        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto("oldPass123", "newPass123");

        doNothing().when(userService).updatePassword(eq(1L), any(UpdatePasswordRequestDto.class));

        mockMvc.perform(patch("/users/{id}/password", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }
}

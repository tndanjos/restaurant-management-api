package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateAddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdatePasswordRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageService messageService;

    private CreateUserRequestDto dto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        dto = new CreateUserRequestDto(
                "João Silva",
                "joao.silva@gmail.com",
                UserType.OWNER,
                new CreateAddressRequestDto(
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
    void shouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User user = userService.createUser(dto);

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo("encodedPass");
        assertThat(user.getEmail()).isEqualTo("joao.silva@gmail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);
        when(messageService.getMessage("email.already.in.use")).thenReturn("Email já está em uso");

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já está em uso");
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByUsername(dto.username())).thenReturn(true);
        when(messageService.getMessage("username.already.in.use")).thenReturn("Username já está em uso");

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username já está em uso");
    }

    @Test
    void shouldGetUserById() {
        User user = new User(dto);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageService.getMessage("user.not.found", 999L)).thenReturn("Usuário não encontrado");

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void shouldReturnPaginatedUsers() {
        User user = new User(dto);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<User> result = userService.getAllUsers(0, 10);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User user = new User(dto);
        user.setId(1L);

        UpdateUserRequestDto updateDto = new UpdateUserRequestDto(
                "Maria",
                "maria@email.com",
                UserType.CUSTOMER,
                dto.address(),
                "mariasilva"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(1L, updateDto);

        assertThat(result.getName()).isEqualTo("Maria");
        assertThat(result.getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = new User(dto);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUserById(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        User user = new User(dto);
        user.setId(1L);
        user.setPassword("oldEncoded");

        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto("oldPassword", "newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "oldEncoded")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncoded");

        userService.updatePassword(1L, dto);

        verify(userRepository).save(argThat(u -> u.getPassword().equals("newEncoded")));
    }

    @Test
    void shouldThrowExceptionWhenOldPasswordIsIncorrect() {
        User user = new User(dto);
        user.setId(1L);
        user.setPassword("oldEncoded");

        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto("wrongPass", "newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "oldEncoded")).thenReturn(false);
        when(messageService.getMessage("old.password.incorrect")).thenReturn("Senha antiga incorreta");

        assertThatThrownBy(() -> userService.updatePassword(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Senha antiga incorreta");
    }
}

package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.AddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class UserTest {

    private User user;

    @BeforeEach
    void setup() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
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

        user = new User(dto);
    }

    @Test
    void shouldCreateUserSuccessfullyFromDto() {
        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getEmail()).isEqualTo("joao.silva@gmail.com");
        assertThat(user.getUsername()).isEqualTo("joaosilva");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getType()).isEqualTo(UserType.OWNER);

        assertThat(user.getAddress().getStreet()).isEqualTo("Rua Augusta");

        assertThat(user.getAddress()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFormatUpdatedAtCorrectly() {
        LocalDateTime specificTime = LocalDateTime.of(2024, 12, 25, 15, 30, 45);
        user.setUpdatedAt(specificTime);

        String formattedDate = user.getFormattedUpdatedAt();
        assertThat(formattedDate).isEqualTo("2024-12-25 15:30:45");
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAtOnPrePersist() {
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.onCreate();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldUpdateUpdatedAtOnPreUpdate() {
        LocalDateTime oldUpdatedAt = user.getUpdatedAt();

        user.onUpdate();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isAfter(oldUpdatedAt);
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertThat(authorities)
                .isNotNull()
                .hasSize(1)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void userAccountShouldBeAlwaysNonExpiredAndEnabled() {
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }
}

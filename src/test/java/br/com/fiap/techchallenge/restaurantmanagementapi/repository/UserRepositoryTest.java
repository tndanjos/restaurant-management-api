package br.com.fiap.techchallenge.restaurantmanagementapi.repository;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateAddressRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
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

        userRepository.save(new User(dto));
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        boolean exists = userRepository.existsByEmail("joao.silva@gmail.com");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseIfEmailNotExists() {
        boolean exists = userRepository.existsByEmail("notexists@gmail.com");
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnTrueIfUsernameExists() {
        boolean exists = userRepository.existsByUsername("joaosilva");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseIfUsernameNotExists() {
        boolean exists = userRepository.existsByUsername("notexists");
        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindUserByUsername() {
        User user = userRepository.findByUsername("joaosilva");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("joaosilva");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldReturnNullWhenFindUserByUsernameNotExists() {
        User user = userRepository.findByUsername("notexists");
        assertThat(user).isNull();
    }
}

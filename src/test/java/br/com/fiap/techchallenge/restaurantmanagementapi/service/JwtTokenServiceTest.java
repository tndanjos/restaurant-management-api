package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    private final String secretKey = "test-secret-key";

    @BeforeEach
    void setup() {
        jwtTokenService = new JwtTokenService();
        ReflectionTestUtils.setField(jwtTokenService, "secretKey", secretKey);
    }

    @Test
    void shouldGenerateValidToken() {
        User user = new User();
        user.setUsername("joaosilva");
        user.setEmail("joao@example.com");
        user.setType(UserType.CUSTOMER);

        String token = jwtTokenService.generateToken(user);
        assertThat(token).isNotNull().isNotEmpty();

        String subject = jwtTokenService.getSubject(token);
        assertThat(subject).isEqualTo("joaosilva");
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThatThrownBy(() -> jwtTokenService.getSubject(invalidToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid or expired JWT token!");
    }
}

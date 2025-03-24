package br.com.fiap.techchallenge.restaurantmanagementapi.filter;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserAuthenticationFilterTest {

    @InjectMocks
    private UserAuthenticationFilter filter;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateUserFromJwtToken() throws Exception {
        String token = "valid.jwt.token";
        String username = "joaosilva";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPass");
        user.setType(UserType.OWNER);

        when(jwtTokenService.getSubject(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        filter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(user);
        assertThat(authentication.getAuthorities()).isEqualTo(user.getAuthorities());

        verify(jwtTokenService).getSubject(token);
        verify(userRepository).findByUsername(username);
    }
}

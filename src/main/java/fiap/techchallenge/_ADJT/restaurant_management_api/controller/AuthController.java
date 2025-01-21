package fiap.techchallenge._ADJT.restaurant_management_api.controller;

import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.LoginRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.dto.response.LoginResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        return new LoginResponseDTO("Authenticated successfully!", authentication.getName());
    }
}




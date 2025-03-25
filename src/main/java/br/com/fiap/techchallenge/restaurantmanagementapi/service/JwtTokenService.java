package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {

    @Value("$jwt.token.secret.key")
    private String secretKey;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("br.com.fiap.techchallenge.restaurantmanagementapi")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new BadCredentialsException("Error generating jwt token", exception);
        }    }

    public String getSubject(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("br.com.fiap.techchallenge.restaurantmanagementapi")
                    .build()
                    .verify(jwtToken)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new BadCredentialsException("Invalid or expired JWT token!");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

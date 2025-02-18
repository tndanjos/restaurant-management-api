package br.com.fiap.techchallenge.restaurantmanagementapi.repository;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
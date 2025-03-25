package br.com.fiap.techchallenge.restaurantmanagementapi.repository;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
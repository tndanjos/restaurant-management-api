package br.com.fiap.techchallenge.restaurantmanagementapi.repository;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> { }

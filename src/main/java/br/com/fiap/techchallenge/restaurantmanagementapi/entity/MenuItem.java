package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "menu_items")
@Entity(name = "MenuItem")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String photoUrl;
    private Boolean dineIn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MenuItem(MenuItemRequestDto dto, Restaurant restaurant) {
        this.name = dto.name();
        this.description = dto.description();
        this.price = dto.price();
        this.photoUrl = dto.photoUrl();
        this.dineIn = dto.dineIn();
        this.restaurant = restaurant;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}

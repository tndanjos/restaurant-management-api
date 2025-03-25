package br.com.fiap.techchallenge.restaurantmanagementapi.entity;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Table(name = "restaurants")
@Entity(name = "Restaurant")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cooking;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalTime openingAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Restaurant(RestaurantRequestDto dto, User user) {
        this.name = dto.name();
        this.cooking = dto.cooking();
        this.address = dto.address() != null ? new Address(dto.address()) : null;
        this.user = user;
        this.openingAt = dto.openingAt();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}

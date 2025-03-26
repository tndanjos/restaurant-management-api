package br.com.fiap.techchallenge.restaurantmanagementapi.dto.response;

import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import java.math.BigDecimal;

public record MenuItemResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String photoUrl,
        Boolean dineIn,
        RestaurantResponseDto restaurant
) {

    public static MenuItemResponseDto fromEntity(MenuItem menuItem) {

        return new MenuItemResponseDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPhotoUrl(),
                menuItem.getDineIn(),
                RestaurantResponseDto.fromEntity(menuItem.getRestaurant())
        );
    }
}

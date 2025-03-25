package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.response.RestaurantResponseDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestBody @Valid RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantService.createRestaurant(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restaurant.getId())
                .toUri();

        return ResponseEntity.created(uri).body(RestaurantResponseDto.fromEntity(restaurant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return ResponseEntity.ok(RestaurantResponseDto.fromEntity(restaurant));
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<Restaurant> restaurants = restaurantService.getAllRestaurants(page, size);

        Page<RestaurantResponseDto> restaurantResponseDTOs = restaurants.map(RestaurantResponseDto::fromEntity);

        return ResponseEntity.ok(restaurantResponseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequestDto dto) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, dto);

        return ResponseEntity.ok(RestaurantResponseDto.fromEntity(updatedRestaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable Long id) {
        restaurantService.deleteRestaurantById(id);

        return ResponseEntity.noContent().build();
    }
}

package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.RestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Address;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.RestaurantRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, MessageService messageService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public Restaurant createRestaurant(RestaurantRequestDto dto) {
        User user = loadOwnerUser(dto.userId());

        Restaurant restaurant = new Restaurant(dto, user);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(Long id) {
        return loadRestaurant(id);
        }

    public Page<Restaurant> getAllRestaurants(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageRequest);
    }

    public Restaurant updateRestaurant(Long id, RestaurantRequestDto dto) {
        User user = loadOwnerUser(dto.userId());
        Restaurant restaurant = loadRestaurant(id);

        restaurant.setName(dto.name());
        restaurant.setCooking(dto.cooking());
        restaurant.setAddress(new Address(dto.address()));
        restaurant.setUser(user);
        restaurant.setOpeningAt(dto.openingAt());

        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurantById(Long id) {
        Restaurant restaurant = loadRestaurant(id);

        restaurantRepository.delete(restaurant);
    }

    private User loadOwnerUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("user.not.found", userId)));

        if (user.getType() == UserType.CUSTOMER) {
            throw new IllegalArgumentException(messageService.getMessage("user.not.allowed"));
        }

        return user;
    }

    private Restaurant loadRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("restaurant.not.found", restaurantId)));
    }
}

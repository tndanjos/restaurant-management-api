package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateRestaurantRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.RestaurantRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Restaurant createRestaurant(CreateRestaurantRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("user.not.found", dto.userId())));

        if (user.getType() == UserType.CUSTOMER) {
            throw new IllegalArgumentException(
                    messageService.getMessage("user.not.allowed")
            );
        }

        Restaurant restaurant = new Restaurant(dto, user);
        return restaurantRepository.save(restaurant);
    }
}

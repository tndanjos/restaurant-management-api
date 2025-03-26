package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.Restaurant;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.MenuItemRepository;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MessageService messageService;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository,
                           RestaurantRepository restaurantRepository,
                           MessageService messageService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.messageService = messageService;
    }

    public MenuItem createMenuItem(MenuItemRequestDto dto) {
        Restaurant restaurant = loadRestaurant(dto.restaurantId());
        MenuItem menuItem = new MenuItem(dto, restaurant);

        return menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(Long id) {
        return loadMenuItem(id);
    }

    public Page<MenuItem> getAllMenuItems(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return menuItemRepository.findAll(pageRequest);
    }

    public MenuItem updateMenuItem(Long id, MenuItemRequestDto dto) {
        MenuItem menuItem = loadMenuItem(id);
        Restaurant restaurant = loadRestaurant(dto.restaurantId());

        menuItem.setName(dto.name());
        menuItem.setDescription(dto.description());
        menuItem.setPrice(dto.price());
        menuItem.setPhotoUrl(dto.photoUrl());
        menuItem.setDineIn(dto.dineIn());
        menuItem.setRestaurant(restaurant);
        menuItem.setUpdatedAt(LocalDateTime.now());

        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItemById(Long id) {
        MenuItem menuItem = loadMenuItem(id);
        menuItemRepository.delete(menuItem);
    }

    private MenuItem loadMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("menuItem.not.found", id)));
    }

    private Restaurant loadRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("restaurant.not.found", restaurantId)));
    }
}

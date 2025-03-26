package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.MenuItemRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.response.MenuItemResponseDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.MenuItem;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@RequestBody @Valid MenuItemRequestDto dto) {
        MenuItem menuItem = menuItemService.createMenuItem(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(menuItem.getId())
                .toUri();

        return ResponseEntity.created(uri).body(MenuItemResponseDto.fromEntity(menuItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);

        return ResponseEntity.ok(MenuItemResponseDto.fromEntity(menuItem));
    }

    @GetMapping
    public ResponseEntity<Page<MenuItemResponseDto>> getAllMenuItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MenuItem> menuItems = menuItemService.getAllMenuItems(page, size);

        Page<MenuItemResponseDto> menuItemResponseDtos = menuItems.map(MenuItemResponseDto::fromEntity);

        return ResponseEntity.ok(menuItemResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(@PathVariable Long id, @RequestBody @Valid MenuItemRequestDto dto) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, dto);

        return ResponseEntity.ok(MenuItemResponseDto.fromEntity(updatedMenuItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable Long id) {
        menuItemService.deleteMenuItemById(id);

        return ResponseEntity.noContent().build();
    }
}

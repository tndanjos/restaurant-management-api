package br.com.fiap.techchallenge.restaurantmanagementapi.controller;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdatePasswordRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.response.UserResponseDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto dto) {
        User user = userService.createUser(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).body(UserResponseDto.fromEntity(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(UserResponseDto.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<User> users = userService.getAllUsers(page, size);

        Page<UserResponseDto> userResponseDTOs = users.map(UserResponseDto::fromEntity);

        return ResponseEntity.ok(userResponseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDto dto) {
        User updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(UserResponseDto.fromEntity(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody @Valid UpdatePasswordRequestDto dto) {
        userService.updatePassword(id, dto);

        return ResponseEntity.ok("Password updated successfully");
    }
}

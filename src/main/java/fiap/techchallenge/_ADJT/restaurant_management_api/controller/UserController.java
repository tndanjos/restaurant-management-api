package fiap.techchallenge._ADJT.restaurant_management_api.controller;

import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.CreateUserRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.UpdatePasswordRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.UpdateUserRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.dto.response.UserResponseDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.entity.User;
import fiap.techchallenge._ADJT.restaurant_management_api.service.UserService;
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
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO dto) {
        User user = userService.createUser(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).body(UserResponseDTO.fromEntity(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);


        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<User> users = userService.getAllUsers(page, size);

        Page<UserResponseDTO> userResponseDTOs = users.map(UserResponseDTO::fromEntity);

        return ResponseEntity.ok(userResponseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDTO dto) {
        User updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(UserResponseDTO.fromEntity(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody @Valid UpdatePasswordRequestDTO dto) {
        userService.updatePassword(id, dto);

        return ResponseEntity.ok("Password updated successfully");
    }
}

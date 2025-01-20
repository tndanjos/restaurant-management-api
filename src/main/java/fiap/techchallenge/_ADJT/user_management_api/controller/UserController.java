package fiap.techchallenge._ADJT.user_management_api.controller;

import fiap.techchallenge._ADJT.user_management_api.dto.request.CreateUserDTO;
import fiap.techchallenge._ADJT.user_management_api.dto.response.UserResponseDTO;
import fiap.techchallenge._ADJT.user_management_api.entity.User;
import fiap.techchallenge._ADJT.user_management_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserDTO dto) {
        User user = userService.createUser(dto);

        return ResponseEntity.status(201).body(UserResponseDTO.fromEntity(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);


        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOs = users.stream().map(UserResponseDTO::fromEntity).toList();

        return ResponseEntity.ok(userResponseDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}

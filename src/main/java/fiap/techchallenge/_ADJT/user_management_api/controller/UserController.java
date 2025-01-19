package fiap.techchallenge._ADJT.user_management_api.controller;

import fiap.techchallenge._ADJT.user_management_api.dto.request.CreateUserDTO;
import fiap.techchallenge._ADJT.user_management_api.entity.User;
import fiap.techchallenge._ADJT.user_management_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserDTO dto) {
        User user = userService.createUser(dto);

        return ResponseEntity.status(201).body(user);
    }
}

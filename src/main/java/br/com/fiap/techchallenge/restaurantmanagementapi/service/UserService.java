package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdatePasswordRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdateUserRequestDto;
import br.com.fiap.techchallenge.restaurantmanagementapi.entity.User;
import br.com.fiap.techchallenge.restaurantmanagementapi.enums.UserType;
import br.com.fiap.techchallenge.restaurantmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageService messageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    public User createUser(CreateUserRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException(messageService.getMessage("email.already.in.use"));
        }
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException(messageService.getMessage("username.already.in.use"));
        }

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = new User(dto);
        user.setPassword(encryptedPassword);

        if (user.getType() != null) {
            user.setType(UserType.valueOf(user.getType().name().toUpperCase()));
        }

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return loadUser(id);
    }

    public Page<User> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(pageRequest);
    }

    public User updateUser(Long id, UpdateUserRequestDto dto) {
        User user = loadUser(id);

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setUsername(dto.username());

        if (user.getType() != null) {
            user.setType(UserType.valueOf(user.getType().name().toUpperCase()));
        }

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = loadUser(id);

        userRepository.delete(user);
    }

    public void updatePassword(Long id, UpdatePasswordRequestDto dto) {
        User user = loadUser(id);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageService.getMessage("old.password.incorrect"));
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    private User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(messageService.getMessage("user.not.found", userId)));
    }
}

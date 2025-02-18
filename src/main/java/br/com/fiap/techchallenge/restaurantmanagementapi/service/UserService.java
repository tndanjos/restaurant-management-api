package br.com.fiap.techchallenge.restaurantmanagementapi.service;

import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.CreateUserRequest;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdatePasswordRequest;
import br.com.fiap.techchallenge.restaurantmanagementapi.dto.request.UpdateUserRequest;
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

    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserRequest dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("The email is already in use.");
        }
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("The username is already in use.");
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
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public Page<User> getAllUsers(int page , int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return userRepository.findAll(pageRequest);
    }

    public User updateUser(Long id, UpdateUserRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setUsername(dto.username());

        if (user.getType() != null) {
            user.setType(UserType.valueOf(user.getType().name().toUpperCase()));
        }

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
    }

    public void updatePassword(Long id, UpdatePasswordRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
}

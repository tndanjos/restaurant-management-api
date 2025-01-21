package fiap.techchallenge._ADJT.restaurant_management_api.service;

import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.CreateUserRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.dto.request.UpdateUserRequestDTO;
import fiap.techchallenge._ADJT.restaurant_management_api.entity.User;
import fiap.techchallenge._ADJT.restaurant_management_api.repository.UserRepository;
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

    public User createUser(CreateUserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("The email is already in use.");
        }
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("The username is already in use.");
        }

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = new User(dto);
        user.setPassword(encryptedPassword);

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

    public User updateUser(Long id, UpdateUserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setUsername(dto.username());

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
    }
}

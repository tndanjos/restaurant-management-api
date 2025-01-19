package fiap.techchallenge._ADJT.user_management_api.service;

import fiap.techchallenge._ADJT.user_management_api.dto.request.CreateUserDTO;
import fiap.techchallenge._ADJT.user_management_api.entity.User;
import fiap.techchallenge._ADJT.user_management_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User createUser(CreateUserDTO dto) {
        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = new User(dto);
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }
}

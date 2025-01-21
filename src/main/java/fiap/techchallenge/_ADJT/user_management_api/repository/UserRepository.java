package fiap.techchallenge._ADJT.user_management_api.repository;

import fiap.techchallenge._ADJT.user_management_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
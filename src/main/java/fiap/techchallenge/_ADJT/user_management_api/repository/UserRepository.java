package fiap.techchallenge._ADJT.user_management_api.repository;

import fiap.techchallenge._ADJT.user_management_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
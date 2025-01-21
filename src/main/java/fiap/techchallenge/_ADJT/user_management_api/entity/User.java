package fiap.techchallenge._ADJT.user_management_api.entity;

import fiap.techchallenge._ADJT.user_management_api.dto.request.CreateUserRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Table(name = "users")
@Entity(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(CreateUserRequestDTO dto) {
            this.name = dto.name();
            this.email = dto.email();
            this.username = dto.username();
            this.password = dto.password();
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
    }

    public String getFormattedUpdatedAt() {
        if (this.updatedAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return this.updatedAt.format(formatter);
        }
        return null;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

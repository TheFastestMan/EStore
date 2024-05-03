package rail.estore.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String passHash;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String passHash, String email, Role role) {
        this.username = username;
        this.passHash = passHash;
        this.email = email;
        this.role = role;
    }
}

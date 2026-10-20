package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Column(nullable = false)
    private LocalDate createdOn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Find> finds = new HashSet<>();

    public void addFind(Find find) {
        if (find != null) {
            this.finds.add(find);
            find.setUser(this);
        }
    }

    public User(String name, String email, String password, Role role, LocalDate createdOn) {
        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
        this.createdOn = createdOn;
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
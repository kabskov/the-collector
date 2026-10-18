package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NonNull
    private String name;

    @Setter
    @NonNull
    @Column(unique = true)
    private String email;

    @Setter
    @NonNull
    private String password;

    @Setter
    @NonNull
    private String role;

    @Setter
    @NonNull
    private LocalDate createdOn;

    @NonNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Find> finds = new HashSet<>();

    public void addFind(Find find) {
        if (find != null) {
            this.finds.add(find);
            find.setUser(this);
        }
    }
}
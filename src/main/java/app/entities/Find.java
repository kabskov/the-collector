package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "finds")
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Find {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NonNull
    @Column(nullable = false)
    private LocalDate date;

    @Setter
    @NonNull
    @Column(nullable = false)
    private String photoURL;

    @Setter
    @NonNull
    @Column(nullable = false)
    private String note;

    @Setter
    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @ManyToMany
    @Column(nullable = false)
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        if (category != null) {
            this.categories.add(category);
        }
    }

    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    private FindSpot findSpot;
}

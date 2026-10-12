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
    private Long id; //Long (wrapper-klasse til long), fordi Long KAN være null. Når jeg opretter et nyt objekt,
    // har det endnu ikke fået et id fra databasen. Id'et genereres af JPA/Hibernate, når objektet
    // gemmes/persisteres. Hvis feltets type er primitiv (long), bliver værdien automatisk sat til 0,
    // og Hibernate kan ikke skelne mellem "Id er 0" og "Id er ikke sat endnu".
    // SÅ! Id-feltet er nødt til at kunne være null, for at det kan få tildelt den rigtige værdi,
    // når objektet gemmes i databasen.

    @Setter
    @NonNull
    private LocalDate date;

    @Setter
    @NonNull
    private String photoURL;

    @Setter
    @NonNull
    private String note;

    @Setter
    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @ManyToMany
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

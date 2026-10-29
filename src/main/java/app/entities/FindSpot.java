package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "find_spots")
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class FindSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private double latitude;

    @NonNull
    @Column(nullable = false)
    private double longitude;

    @NonNull
    @Column(nullable = false, length = 500)
    private String name;
}

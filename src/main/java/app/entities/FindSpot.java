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
    private double latitude;

    @NonNull
    private double longitude;

    @NonNull
    @Column(length = 500)
    private String name;
}

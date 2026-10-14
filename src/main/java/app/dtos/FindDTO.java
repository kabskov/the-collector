package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindDTO {

    private LocalDate date;
    private String note;
    private String photoURL;
    private Long userId;
    private List<Integer> categoryIds;
    private String findSpotKeyword;
    //FindDTO: dato, note, photoURL, userId, kategori-id’er (en liste), søgeord til sted. Det er det, der skal ind.
    // Hvad skal der med ud, fx stednavn og id? Hvis du bruger én DTO begge veje, skal de felter også med.
    private String findSpotName;
    private Long findId;
}

package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindDTO {

    private Long findId;
    private LocalDate date;
    private String note;
    private String photoURL;
    private Long userId;
    private List<Long> categoryIds;
    private String findSpotKeyword;
    private String findSpotName;
}

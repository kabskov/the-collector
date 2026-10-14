package app.mappers;

import app.dtos.FindDTO;
import app.entities.Find;
import app.entities.User;

public class FindMapper {

    public static Find convertToFind(FindDTO findDTO, User user) {
        return new Find();
    }

    public static FindDTO convertToFindDTO(Find find) {
        return new FindDTO();
    }
}

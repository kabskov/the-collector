package app.mappers;

import app.dtos.FindDTO;
import app.entities.Category;
import app.entities.Find;
import app.entities.User;

import java.util.List;

public class FindMapper {

    public static Find convertToFind(FindDTO findDTO, User user) {
        String note = findDTO.getNote() != null ? findDTO.getNote() : "";
        return new Find(findDTO.getDate(), findDTO.getPhotoURL(), note, user);
    }

    public static FindDTO convertToFindDTO(Find find) {
        List<Long> categoryIds = find.getCategories().stream().map(Category::getId).toList();
        String findSpotName = (find.getFindSpot() != null) ? find.getFindSpot().getName() : null;
        return new FindDTO(find.getId(), find.getDate(), find.getNote(), find.getPhotoURL(), find.getUser().getId(), categoryIds, null, findSpotName);
    }
}

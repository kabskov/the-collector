package app.services;

import app.dtos.FindDTO;
import app.entities.Category;
import app.entities.Find;
import app.entities.FindSpot;
import app.entities.User;
import app.mappers.FindMapper;

import java.util.Set;

public class FindService {

    FindSpotService findSpotService = new FindSpotService();

    //Hvilke oplysninger skal den modtage for at oprette et fund?
    // Tænk på, hvad et Find består af, og hvad der skal til for at finde brugeren og stedet.
    //Hvilke af dine eksisterende klasser skal den bruge, og i hvilken rækkefølge?
    //Hvad skal der ske, hvis brugeren ikke har angivet et sted? Din model tillader jo et Find uden FindSpot.

    //For at oprette et fund skal jeg bruge:
    // - Dato
    // - Note
    // - Billede (URL)
    // - Bruger (klasse)
    // - Fundsted er valgfrit (klasse)
    // - Kategori (klasse)


 /*   public FindDTO registerFind(FindDTO findDTO) {
        User user = userDAO.getUserById(findDTO.getUserId);
        Set<Category> categories = categoryDAO.getCategoryById(findDTO.getCategories.getId);
        Find find = FindMapper.convertToFind(findDTO, user);
        for (Category category : categories) {
            find.addCategory(category);
        }
        if (findDTO.getFindSpot != null) {
            FindSpot findSpot = findSpotService.getFindSpotDetails(findDTO.getFindSpot);
            find.setFindSpot(findSpot);
        }
        return FindMapper.convertToFindDTO(find);
    }*/

    //  public PoemDTO createPoem(PoemDTO poemDTO) {
    //        Poem poem = PoemMapper.poemDTOToPoem(null, poemDTO);
    //        poemDAO.create(poem);
    //        return PoemMapper.poemToPoemDTO(poem);
    //    }
}

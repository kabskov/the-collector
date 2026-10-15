package app.services;

import app.config.HibernateConfig;
import app.daos.CategoryDAO;
import app.daos.FindDAO;
import app.daos.UserDAO;
import app.dtos.FindDTO;
import app.dtos.FindSpotDTO;
import app.entities.Category;
import app.entities.Find;
import app.entities.User;
import app.mappers.FindMapper;
import app.mappers.FindSpotMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class FindService {

    private final FindSpotService findSpotService = new FindSpotService();
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final FindDAO findDAO;

    public FindService(EntityManagerFactory emf) {
        this.userDAO = new UserDAO(emf);
        this.categoryDAO = new CategoryDAO(emf);
        this.findDAO = new FindDAO(emf);
    }

    public FindDTO registerFind(FindDTO findDTO) throws Exception {
        User user = userDAO.getById(findDTO.getUserId());
        List<Long> categoryIds = findDTO.getCategoryIds();
        Find find = FindMapper.convertToFind(findDTO, user);
        String findSpotKeyword = findDTO.getFindSpotKeyword();

        for (Long id : categoryIds) {
            Category category = categoryDAO.getById(id);
            find.addCategory(category);
        }

        if (findSpotKeyword != null && !findSpotKeyword.isBlank()) {
            FindSpotDTO findSpotDTO = findSpotService.getFindSpotDetails(findSpotKeyword);
            find.setFindSpot(FindSpotMapper.convertToFindSpot(findSpotDTO));
        }

        findDAO.create(find);

        return FindMapper.convertToFindDTO(find);
    }
}

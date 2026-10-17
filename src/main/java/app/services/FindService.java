package app.services;

import app.daos.CategoryDAO;
import app.daos.FindDAO;
import app.daos.UserDAO;
import app.dtos.FindDTO;
import app.dtos.FindSpotDTO;
import app.entities.Category;
import app.entities.Find;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.FindMapper;
import app.mappers.FindSpotMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
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

    public FindDTO registerFind(FindDTO findDTO) {
        List<Long> categoryIds = findDTO.getCategoryIds();
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new ApiException(400, "A find must have at least one category");
        }
        if (findDTO.getUserId() == null) {
            throw new ApiException(400, "User id is required");
        }
        if (findDTO.getDate() == null) {
            throw new ApiException(400, "Date is required");
        }
        if (findDTO.getPhotoURL() == null || findDTO.getPhotoURL().isBlank()) {
            throw new ApiException(400, "Photo URL is required");
        }
        User user = userDAO.getById(findDTO.getUserId());
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

    public FindDTO getFindByIdAndUserId(Long userId, Long findId) {
        FindDTO findDTO = FindMapper.convertToFindDTO(findDAO.getById(findId));
        if (!findDTO.getUserId().equals(userId)) {
            throw new ApiException(404, "Find with id " + findId + " not found");
        }
        return findDTO;
    }

    public List<FindDTO> getAllFindsByUserId(Long userId) {
        userDAO.getById(userId);
        List<Find> finds = findDAO.getAllFindsByUserId(userId);
        List<FindDTO> findDTOS = new ArrayList<>();
        for (Find find : finds) {
            findDTOS.add(FindMapper.convertToFindDTO(find));
        }
        return findDTOS;
    }
}

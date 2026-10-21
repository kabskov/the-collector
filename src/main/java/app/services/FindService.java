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

    public FindDTO registerFind(Long userId, FindDTO findDTO) {
        validate(findDTO);
        User user = userDAO.getById(userId);
        Find find = FindMapper.convertToFind(findDTO, user);
        String findSpotKeyword = findDTO.getFindSpotKeyword();

        List<Long> categoryIds = findDTO.getCategoryIds();
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

    public FindDTO updateFind(Long userId, Long findId, FindDTO findDTO) {
        //Tjekker, om fundet tilhører brugeren. Hvis ikke -> 404.
        getFindByIdAndUserId(userId, findId);
        Find find = findDAO.getById(findId);
        if (findDTO.getDate() != null) {
            find.setDate(findDTO.getDate());
        }
        if (findDTO.getPhotoURL() != null) {
            find.setPhotoURL(findDTO.getPhotoURL());
        }
        if (findDTO.getNote() != null) {
            find.setNote(findDTO.getNote());
        }
        if (findDTO.getCategoryIds() != null) {
            if (findDTO.getCategoryIds().isEmpty()) {
                throw new ApiException(400, "A find must have at least one category");
            }
            find.getCategories().clear();
            List<Long> categoryIds = findDTO.getCategoryIds();
            for (Long id : categoryIds) {
                Category category = categoryDAO.getById(id);
                find.addCategory(category);
            }
        }

        findDAO.update(find);
        return getFindByIdAndUserId(userId, findId);
    }

    public void deleteFind(Long userId, Long findId) {
        getFindByIdAndUserId(userId, findId);
        findDAO.delete(findId);
    }

    private void validate(FindDTO findDTO) {
        List<Long> categoryIds = findDTO.getCategoryIds();
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new ApiException(400, "A find must have at least one category");
        }
        if (findDTO.getDate() == null) {
            throw new ApiException(400, "Date is required");
        }
        if (findDTO.getPhotoURL() == null || findDTO.getPhotoURL().isBlank()) {
            throw new ApiException(400, "Photo URL is required");
        }
    }
}

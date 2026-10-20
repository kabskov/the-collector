package app.services;

import app.daos.UserDAO;
import app.dtos.LoginDTO;
import app.dtos.UserDTO;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.UserMapper;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class UserService {

    private final UserDAO userDAO;

    public UserService(EntityManagerFactory emf) {
        this.userDAO = new UserDAO(emf);
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().isBlank()) {
            throw new ApiException(400, "Name is required");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new ApiException(400, "Email is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new ApiException(400, "Password is required");
        }

        if (userDAO.getByEmail(userDTO.getEmail()) != null) {
            throw new ApiException(409, "Email already in use");
        }

        User user = UserMapper.convertToUser(userDTO);
        userDAO.create(user);
        return UserMapper.convertToUserDTO(user);
    }

    public UserDTO login(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isBlank()) {
            throw new ApiException(400, "Email is required");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isBlank()) {
            throw new ApiException(400, "Password is required");
        }
        User user = userDAO.getByEmail(loginDTO.getEmail());
        if (user == null || !user.verifyPassword(loginDTO.getPassword())) {
            throw new ApiException(401, "Wrong email or password");
        }

        return UserMapper.convertToUserDTO(user);
    }

    public UserDTO getUserById(Long id) {
        return UserMapper.convertToUserDTO(userDAO.getById(id));
    }
}

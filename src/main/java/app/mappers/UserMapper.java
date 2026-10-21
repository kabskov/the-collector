package app.mappers;

import app.dtos.UserDTO;
import app.entities.Role;
import app.entities.User;

import java.time.LocalDate;

public class UserMapper {

    public static User convertToUser(UserDTO userDTO) {
        return new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), Role.USER, LocalDate.now());
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), null, user.getRole(), user.getCreatedOn());
    }
}

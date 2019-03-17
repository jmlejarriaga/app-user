package com.management.appuser.management;

import com.management.appuser.exception.UserBadRequestException;
import com.management.appuser.exception.UserNotFoundException;
import com.management.appuser.model.User;
import com.management.appuser.model.UserDTO;
import com.management.appuser.model.UserPatchDTO;
import com.management.appuser.model.UserPostDTO;
import com.management.appuser.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
public class UserManagementService {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User '%s' not found!";
    private static final String USER_ID_NOT_NUMERIC = "User id '%s' is not numeric!";
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserService userService;

    public ResponseEntity<List<UserDTO>> getAllExistingUsers() {
        Optional<List<User>> listOptionalUser = userService.getAllUsers();
        if (!listOptionalUser.isPresent()) {
            throw new UserNotFoundException("No users found!");
        }
        return new ResponseEntity<>(convertToUserDTOList(listOptionalUser.get()), OK);
    }

    public ResponseEntity<UserDTO> getOneUser(String userId) {
        checkIdException(userId);
        Optional<User> optionalUser = userService.getUser(Integer.parseInt(userId));
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, userId));
        }
        return new ResponseEntity<>(convertToUserDTO(optionalUser.get()), OK);
    }

    public ResponseEntity<UserDTO> createUser(UserPostDTO newUser) {
        checkInputData(newUser);

        User user = converToUser(newUser);
        user.setCreatedAt(LocalDateTime.now());
        Optional<User> optionalUser = userService.createNewUser(user);
        if (!optionalUser.isPresent()) {
            throw new UserBadRequestException(String.format("User '%s' could not be created!", newUser.getId()));
        }
        return new ResponseEntity<>(convertToUserDTO(optionalUser.get()), CREATED);
    }

    public ResponseEntity<UserDTO> updateUser(String userId, UserPatchDTO modifiedUserData) {
        checkIdException(userId);

        Optional<User> optionalUser = userService.getUser(Integer.parseInt(userId));
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, userId));
        }
        modifyUserData(modifiedUserData, optionalUser.get());

        Optional<User> optionalModifiedUser = userService.updateUser(optionalUser.get());
        if (!optionalModifiedUser.isPresent()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, userId));
        }
        return new ResponseEntity<>(convertToUserDTO(optionalUser.get()), OK);
    }

    public ResponseEntity<UserDTO> deleteUser(String userId) {
        if (!StringUtils.isNumeric(userId)) {
            throw new UserBadRequestException(String.format(USER_ID_NOT_NUMERIC, userId));
        }
        int id = Integer.parseInt(userId);
        Optional<User> optionalUser = userService.getUser(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, userId));
        }

        userService.deleteUser(optionalUser.get());
        return new ResponseEntity<>(convertToUserDTO(optionalUser.get()), OK);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User converToUser(UserPostDTO userPostDTO) {
        return modelMapper.map(userPostDTO, User.class);
    }

    private List<UserDTO> convertToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private void checkIdException(String userId) {
        if (!StringUtils.isNumeric(userId)) {
            throw new UserBadRequestException(String.format(USER_ID_NOT_NUMERIC, userId));
        }
    }

    private void checkInputData(UserPostDTO newUser) {
        StringBuilder message = new StringBuilder(StringUtils.EMPTY);

        if (isInvalidId(newUser)) {
            message.append(String.format("Id: '%s'; ", newUser.getId()));
        }
        if (isNull(newUser.getName())) {
            message.append(String.format("Name: '%s'; ", newUser.getName()));
        }
        if (isNull(newUser.getBirthDate())) {
            message.append(String.format("Birth date: '%s'; ", newUser.getBirthDate()));
        }
        if (!StringUtils.EMPTY.equals(message.toString())) {
            throw new UserBadRequestException(message.toString());
        }
    }

    private boolean isInvalidId(UserPostDTO newUser) {
        return isNull(newUser.getId()) || !StringUtils.isNumeric(newUser.getId());
    }

    private void modifyUserData(UserPatchDTO modifiedUserData, User user) {
        if (!isNull(modifiedUserData.getName())) {
            user.setName(modifiedUserData.getName());
        }
        if (!isNull(modifiedUserData.getBirthDate())) {
            user.setBirthDate(LocalDate.parse(modifiedUserData.getBirthDate()));
        }
    }

    private <T> boolean isNull(T object) {
        return null == object;
    }
}

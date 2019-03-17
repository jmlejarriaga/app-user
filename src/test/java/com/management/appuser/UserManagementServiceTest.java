package com.management.appuser;

import com.management.appuser.exception.UserBadRequestException;
import com.management.appuser.exception.UserNotFoundException;
import com.management.appuser.management.UserManagementService;
import com.management.appuser.model.User;
import com.management.appuser.model.UserDTO;
import com.management.appuser.model.UserPostDTO;
import com.management.appuser.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class UserManagementServiceTest extends TestUtil {

    @InjectMocks
    private UserManagementService userManagementService;

    @Mock
    private UserService userService;

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnNotFoundException_when_dataBaseIsEmpty() {
        when(userService.getAllUsers()).thenReturn(Optional.empty());

        userManagementService.getAllExistingUsers();
    }

    @Test
    public void shouldReturnListOfUsers_when_dataBaseIsFull() {
        List<User> expectedList = aListOfUsers();
        when(userService.getAllUsers()).thenReturn(Optional.of(expectedList));

        ResponseEntity<List<UserDTO>> response = userManagementService.getAllExistingUsers();

        assertEquals(expectedList.size(), response.getBody().size());
        assertEquals("name1", response.getBody().get(0).getName());
        assertEquals("name2", response.getBody().get(1).getName());
    }

    @Test
    public void shouldReturnUser_when_providedUserIdExists() {
        when(userService.getUser(anyInt())).thenReturn(Optional.of(aUser(1, "Pepe", "1980-01-01")));

        ResponseEntity<UserDTO> response = userManagementService.getOneUser("1");

        assertEquals("Pepe", response.getBody().getName());
    }

    @Test(expected = UserBadRequestException.class)
    public void shouldReturnBadRequestException_when_providedUserIdIsNotNumeric() {
        userManagementService.getOneUser("zzz");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnNotFound_when_providedUserIdDoesNotExist() {
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        userManagementService.getOneUser("1");
    }

    @Test(expected = UserBadRequestException.class)
    public void shouldReturnBadRequestException_when_updatingUserIdIsNotNumeric() {
        userManagementService.updateUser("zzz", aModifiedUser("Any", "1111-22-33"));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnNotFound_when_updatingNotExistingUser() {
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        userManagementService.updateUser("1", aModifiedUser("Any", "1111-22-33"));
    }

    @Test
    public void shouldCreateNewUser_when_inputIsOk() {
        User newUser = aUser(1, "name", "1980-01-01");
        when(userService.createNewUser(any(User.class))).thenReturn(Optional.of(newUser));

        UserPostDTO userPostDTO = UserPostDTO.builder()
                .id(String.valueOf(newUser.getId()))
                .name(newUser.getName())
                .birthDate(newUser.getBirthDate())
                .build();
        ResponseEntity<UserDTO> response = userManagementService.createUser(userPostDTO);

        assertEquals(CREATED, response.getStatusCode());
    }

    @Test(expected = UserBadRequestException.class)
    public void shouldReturn500_when_userNotCreated() {
        User newUser = aUser(1, "name", "1980-01-01");
        when(userService.createNewUser(any(User.class))).thenReturn(Optional.empty());
        UserPostDTO userPostDTO = UserPostDTO.builder()
                .id(String.valueOf(newUser.getId()))
                .name(newUser.getName())
                .birthDate(newUser.getBirthDate())
                .build();

        userManagementService.createUser(userPostDTO);
    }

    @Test(expected = UserBadRequestException.class)
    public void shouldReturn400_when_newUserHasInvalidData() {
        User newUser = aUser(1, null, "1980-01-01");
        UserPostDTO userPostDTO = UserPostDTO.builder()
                .id(null)
                .name(null)
                .birthDate(newUser.getBirthDate())
                .build();

        userManagementService.createUser(userPostDTO);
    }

    @Test
    public void shouldModifyData_when_updatingExistingUser() {
        User existingUser = aUser(1, "name", "1980-01-01");
        User modifiedUser = aUser(1, "newName", "1980-01-30");
        when(userService.getUser(anyInt())).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(any(User.class))).thenReturn(Optional.of(modifiedUser));

        ResponseEntity<UserDTO> response = userManagementService.updateUser("1", aModifiedUser("newName", "1980-01-30"));

        assertEquals(OK, response.getStatusCode());
        assertEquals("newName", response.getBody().getName());
        assertEquals("1980-01-30", response.getBody().getBirthDate().format(DateTimeFormatter.ISO_DATE));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnNotFound_when_deletingNotExistingUser() {
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        userManagementService.deleteUser("1");
    }

    @Test(expected = UserBadRequestException.class)
    public void shouldReturnBadRequestException_when_deletingUserIdIsNotNumeric() {
        userManagementService.deleteUser("zzz");
    }

    @Test
    public void shouldReturnOk_when_deletingExistingUser() {
        User userToDelete = aUser(1, "name", "1980-01-01");
        when(userService.getUser(anyInt())).thenReturn(Optional.of(userToDelete));

        ResponseEntity<UserDTO> response = userManagementService.deleteUser("1");

        assertEquals(OK, response.getStatusCode());
    }
}

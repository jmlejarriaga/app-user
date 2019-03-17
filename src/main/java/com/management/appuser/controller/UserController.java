package com.management.appuser.controller;

import com.management.appuser.management.UserManagementService;
import com.management.appuser.model.UserDTO;
import com.management.appuser.model.UserPatchDTO;
import com.management.appuser.model.UserPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/app-user")
public class UserController {

    private static final String ENDPOINT_URI = "/user";
    private static final String ENDPOINT_VARIABLE = "/{user-id}";

    @Autowired
    private UserManagementService userManagementService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllExistingUsers() {
        return userManagementService.getAllExistingUsers();
    }

    @GetMapping(value = ENDPOINT_URI + ENDPOINT_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable(value = "user-id") String userId) {
        return userManagementService.getOneUser(userId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserPostDTO newUser) {
        return userManagementService.createUser(newUser);
    }

    @PatchMapping(value = ENDPOINT_URI + ENDPOINT_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "user-id") String userId,
                                              @Valid @RequestBody UserPatchDTO modifiedUserData) {
        return userManagementService.updateUser(userId, modifiedUserData);
    }

    @DeleteMapping(value = ENDPOINT_URI + ENDPOINT_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> deleteUser(@PathVariable(value = "user-id") String userId) {
        return userManagementService.deleteUser(userId);
    }
}

package com.management.appuser.service;

import com.management.appuser.model.User;
import com.management.appuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<List<User>> getAllUsers() {
        List<User> userList = (List<User>) userRepository.findAll();
        if(userList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userList);
    }

    public Optional<User> getUser(int userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> createNewUser(User newUser) {
        return Optional.of(userRepository.save(newUser));
    }

    public Optional<User> updateUser(User modifiedUser) {
        return Optional.of(userRepository.save(modifiedUser));
    }

    public void deleteUser(User deletedUser) {
        userRepository.delete(deletedUser);
    }
}

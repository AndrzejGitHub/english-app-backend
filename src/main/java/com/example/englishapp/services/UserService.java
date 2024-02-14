package com.example.englishapp.services;

import com.example.englishapp.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();

    Optional<User> getUser(Integer id);

    User insertUser(User user);

    User updateUser(Integer id, User user);

    void deleteUser(Integer id);

    Optional<User> findByEmail(String email);
}

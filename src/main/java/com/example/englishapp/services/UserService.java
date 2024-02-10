package com.example.englishapp.services;

import com.example.englishapp.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUser(Integer id);

    User insertUser(User user);

    User updateUser(Integer id, User user);

    void deleteUser(Integer id);
}

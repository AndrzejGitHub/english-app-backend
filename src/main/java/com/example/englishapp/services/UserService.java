package com.example.englishapp.services;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.User;
import com.example.englishapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get(); }
        else throw new NotFoundException("User was not found");
    }

    public User insertUser(User user) {
        System.out.println(user);
        if (Objects.isNull(user.getId()))
            return userRepository.save(user);
        else throw new ConflictException();
    }

    public User updateUser(Integer id, User user) {
        if (userRepository.findById(id).isPresent()) {
            user.setId(id);
//            return userRepository.save(user).getId();
            return userRepository.save(user);
        }
        else throw new NotFoundException("User was not found");
    }

    public void deleteUser(Integer id) {
        if (userRepository.findById(id).isPresent())
            userRepository.deleteById(id);
           else throw new NotFoundException("User was not found");
    }
}

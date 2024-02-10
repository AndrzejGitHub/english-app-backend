package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.User;
import com.example.englishapp.repositories.UserRepository;
import com.example.englishapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(() -> new NotFoundException("User was not found"));
    }

    @Override
    public User insertUser(User user) {
        if (Objects.isNull(user.getId()))
            return userRepository.save(user);
        else throw new ConflictException("Conflict. User exist.");
    }

    @Override
    public User updateUser(Integer id, User user) {
        if (userRepository.findById(id).isPresent()) {
            user.setId(id);
            return userRepository.save(user);
        } else throw new NotFoundException("User was not found");
    }

    @Override
    public void deleteUser(Integer id) {
        if (userRepository.findById(id).isPresent())
            userRepository.deleteById(id);
        else throw new NotFoundException("User was not found");
    }
}

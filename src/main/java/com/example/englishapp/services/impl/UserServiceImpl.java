package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.User;
import com.example.englishapp.repositories.UserRepository;
import com.example.englishapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(Integer id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User was not found")));
    }

    @Override
    public User insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return (User) Optional.ofNullable(user.getId())
                .map(userId -> { throw new ConflictException("Conflict. User exist."); })
                .orElse(userRepository.save(user));
    }

    @Override
    public User updateUser(Integer id, User user) {
        return userRepository.findById(id)
                .map(userFromDb -> {
                    user.setId(id);
                    user.setPassword(userFromDb.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new NotFoundException("User was not found"));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> userRepository.deleteById(id),
                        () -> {
                            throw new NotFoundException("User was not found");
                        }
                );
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

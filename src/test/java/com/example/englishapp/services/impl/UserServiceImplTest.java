package com.example.englishapp.services.impl;

import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.User;
import com.example.englishapp.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    @Test
    void getUsers() {
        // given
        // when
        userService.getUsers();
        // then
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void getUser_UserExists() {
        // given
        Integer id = 1;
        User user = User.builder().build();
        Optional<User> optionalUser = Optional.of(user);

        // when
        Mockito.when(userRepository.findById(anyInt())).thenReturn(optionalUser);
        Optional<User> response = userService.getUser(id);

        // then
        assertEquals(optionalUser, response);
        Mockito.verify(userRepository).findById(id);
    }



    @Test
    void getUser_NotFound() {
        //given
        Integer id = 1;
        //when
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //then
        assertThrows((NotFoundException.class), () -> userService.getUser(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void insertUser_IdIsNull() {
        //given
        User user = User.builder()
                .firstName("name")
                .lastName("surname")
                .build();
        //when
        Mockito.when(userRepository.save(user)).thenReturn(user);
        var response = userService.insertUser(user);
        //then
        assertEquals(user, response);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void insertUser_IdIsNotNull() {
        //given
        User user = User.builder()
                .id(1)
                .firstName("name")
                .lastName("surname")
                .build();
        assertThrows((ConflictException.class), () -> userService.insertUser(user));
    }

    @Test
    void updateUser_UserExists() {
        // given
        Integer id = 1;
        User user = User.builder()
                .id(id)
                .firstName("name")
                .lastName("surname")
                .build();
        Optional<User> optionalUser = Optional.of(user);
        // when
        Mockito.when(userRepository.findById(id)).thenReturn(optionalUser);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User response = userService.updateUser(id, user);
        // then
        assertEquals(user, response);
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).save(user);
        Assertions.assertThat(response).isEqualTo(user);
    }

    @Test
    void updateUser_UserNotFound() {
        // given
        Integer id = 1;
        User user = User.builder()
                .firstName("name")
                .lastName("surname")
                .build();
        Optional<User> optionalUser = Optional.empty();
        // when
        Mockito.when(userRepository.findById(id)).thenReturn(optionalUser);
        // then
        assertThrows(NotFoundException.class, () -> userService.updateUser(id, user));
        Mockito.verify(userRepository).findById(id);
    }


    @Test
    void deleteUser_UserExists() {
        // given
        Integer id = 1;
        Optional<User> optionalUser = Optional.of(User.builder().build());
        // when
        Mockito.when(userRepository.findById(id)).thenReturn(optionalUser);
        userService.deleteUser(id);
        // then
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_UserNotFound() {
        // given
        Integer id = 1;
        Optional<User> optionalUser = Optional.empty();
        // when
        Mockito.when(userRepository.findById(id)).thenReturn(optionalUser);
        // then
        assertThrows(NotFoundException.class, () -> userService.deleteUser(id));
        Mockito.verify(userRepository).findById(id);
    }

}
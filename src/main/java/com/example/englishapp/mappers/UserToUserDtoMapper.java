package com.example.englishapp.mappers;

import com.example.englishapp.models.User;
import com.example.englishapp.models.dto.UserDto;

public class UserToUserDtoMapper {

    public static UserDto convertTo(User user) {

        return UserDto.builder().
                id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(
                        user.getRoles())
                .build();
    }

    public static User converTo(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .roles(userDto.getRoles())
                .build();
    }
}

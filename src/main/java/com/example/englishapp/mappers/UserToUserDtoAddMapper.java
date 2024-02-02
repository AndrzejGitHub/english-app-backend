package com.example.englishapp.mappers;

import com.example.englishapp.models.User;
import com.example.englishapp.models.dto.UserDtoAdd;

public class UserToUserDtoAddMapper {

    public static UserDtoAdd convertTo(User user) {

        return UserDtoAdd.builder().
                id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

    public static User converTo(UserDtoAdd userDtoAdd) {
        return User.builder()
                .id(userDtoAdd.getId())
                .firstName(userDtoAdd.getFirstName())
                .lastName(userDtoAdd.getLastName())
                .email(userDtoAdd.getEmail())
                .password(userDtoAdd.getPassword())
                .roles(userDtoAdd.getRoles())
                .build();
    }
}

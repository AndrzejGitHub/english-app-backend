package com.example.englishapp.controllers;

import com.example.englishapp.mappers.UserToUserDtoMapper;
import com.example.englishapp.models.User;
import com.example.englishapp.models.dto.IdDto;
import com.example.englishapp.models.dto.UserDto;
import com.example.englishapp.models.dto.UserDtoAdd;
import com.example.englishapp.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserRestController {

    final UserService userService;
    final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getUsers().stream().map((user ->  modelMapper.map(user, UserDto.class))).toList());
//                userService.getUsers().stream().map(userMapper::convertToDto).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserToUserDtoMapper.convertTo(userService.getUser(id)));
    }


    @PostMapping()
    public ResponseEntity<UserDtoAdd> addUser(@Valid @RequestBody UserDtoAdd userDtoAdd) {
        System.out.println(userDtoAdd);
        var user = userService.insertUser(modelMapper.map(userDtoAdd, User.class));
        if (user.getId()==null) //check correctness of saving
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        else
            return ResponseEntity.status(HttpStatus.OK).body(
                    modelMapper.map(user, UserDtoAdd.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        var user = userService.updateUser(id, modelMapper.map(userDto, User.class));
        return ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(user, UserDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IdDto> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(IdDto.builder().id(id).build());
    }
}

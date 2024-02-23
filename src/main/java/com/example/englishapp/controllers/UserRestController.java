package com.example.englishapp.controllers;

import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.User;
import com.example.englishapp.models.dto.IdDto;
import com.example.englishapp.models.dto.UserDto;
import com.example.englishapp.models.dto.UserDtoAdd;
import com.example.englishapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    final HttpServletRequest httpServletRequest;


    @GetMapping("/who-am-i")
    public ResponseEntity<User> whoAmI(Principal principal) {
        return userService.findByEmail(principal.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("User was not found"));
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getUsers().stream().map((user -> modelMapper.map(user, UserDto.class))).toList());
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        return Optional.ofNullable(userService.getUser(id))
                .map(user -> ResponseEntity.ok().body(modelMapper.map(user, UserDto.class)))
                .orElseThrow(() -> new NotFoundException("User was not found"));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping()
    public ResponseEntity<UserDtoAdd> addUser(@Valid @RequestBody UserDtoAdd userDtoAdd) {
        return Optional.ofNullable(userService.insertUser(modelMapper.map(userDtoAdd, User.class)))
                .map(user -> ResponseEntity.status(HttpStatus.OK).body(
                        modelMapper.map(user, UserDtoAdd.class)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDtoAdd));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                modelMapper.map(userService.updateUser(id, modelMapper.map(userDto, User.class)), UserDto.class));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<IdDto> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(IdDto.builder().id(id).build());
    }
}

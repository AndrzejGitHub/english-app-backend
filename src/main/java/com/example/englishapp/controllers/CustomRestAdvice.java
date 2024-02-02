package com.example.englishapp.controllers;


import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.dto.ErrorDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomRestAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleErrors(MethodArgumentNotValidException exception) {
        var errors = exception.getAllErrors().stream().map(
                DefaultMessageSourceResolvable::getDefaultMessage
        ).toList();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorDto.builder()
                .messages(errors)
                .build());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflict(ConflictException conflictException) {
        var errors = conflictException.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorDto.builder()
                        .messages(List.of(errors))
                        .build()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleConflict(NotFoundException notFoundException) {
        var errors = notFoundException.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorDto.builder()
                        .messages(List.of(errors))
                        .build()
        );
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDto> handleGenericException(Exception exception) {
//        var errors = exception.getMessage();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                ErrorDto.builder()
//                        .messages(List.of(errors))
//                        .build()
//        );
//    }

}
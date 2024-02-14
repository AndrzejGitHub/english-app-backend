package com.example.englishapp.controllers;

import com.example.englishapp.exeptions.BadRequestException;
import com.example.englishapp.exeptions.ConflictException;
import com.example.englishapp.exeptions.NotFoundException;
import com.example.englishapp.models.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.IntStream;

@AllArgsConstructor
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    final HttpServletRequest httpServletRequest;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        var dtoClass = Objects.requireNonNull(methodArgumentNotValidException.getParameter().getMethod()).getParameters()[0].getType();
        List<String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparingInt(fieldError -> getFieldOrder(fieldError, dtoClass)))
                .map(fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), ""))
                .toList();
        return ResponseEntity.badRequest().body(ErrorDto.builder().messages(errors).build());
    }

    private int getFieldOrder(FieldError fieldError, Class<?> dtoClass) {
        try {
            String fieldName = fieldError.getField();
            Field[] fields = dtoClass.getDeclaredFields();
            OptionalInt indexOptional = IntStream.range(0, fields.length)
                    .filter(i -> fields[i].getName().equals(fieldName))
                    .map(i -> i + 1)
                    .findFirst();
            return indexOptional.orElse(Integer.MAX_VALUE);
        } catch (Exception exception) {
            logger.error("Error occurred while retrieving field order", exception);
            return Integer.MAX_VALUE;
        }
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflictException(ConflictException conflictException) {
        var errors = conflictException.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorDto.builder()
                        .messages(List.of(errors))
                        .build()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException notFoundException) {
        var errors = notFoundException.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorDto.builder()
                        .messages(List.of(errors))
                        .build()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        var errors = noSuchElementException.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorDto.builder()
                        .messages(List.of(errors))
                        .build()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorDto.builder()
                        .messages(List.of(exception.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDto> handleAllExceptions(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorDto.builder()
                        .messages(List.of(getRequestDetails(request), exception.getMessage()))
                        .build()
        );
    }

    private String getRequestDetails(WebRequest request) {
        return "URL: " + request.getDescription(false) + "\n";
    }

}
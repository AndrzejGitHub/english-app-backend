package com.example.englishapp.models.dto;

import lombok.*;

import java.util.List;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorDto {
    private List<String> messages;
}

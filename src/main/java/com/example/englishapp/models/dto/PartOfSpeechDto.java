package com.example.englishapp.models.dto;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PartOfSpeechDto {

    private Integer id;

    private String name;
}

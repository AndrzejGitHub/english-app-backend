package com.example.englishapp.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VocabularyDto {

    private Integer id;

    @NotNull(message = "english word is required")
    @Size(min = 1, max = 255, message = "English word must be between 1 and 255 characters long")
    private String englishWord;

    private String imageURL;

}


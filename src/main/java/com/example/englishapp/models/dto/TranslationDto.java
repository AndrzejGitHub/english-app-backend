package com.example.englishapp.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TranslationDto {
    private Integer id;

    @NotNull(message = "Part of speech is required")
    private PartOfSpeechDto partOfSpeech;

    @NotNull(message = "English word is required")
    private VocabularyDto vocabulary;

    @NotNull(message = "Polish translation is required")
    private TranslationVariantDto translationVariant;

}

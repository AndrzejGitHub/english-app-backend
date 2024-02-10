package com.example.englishapp.models.dto;

import com.example.englishapp.models.PartOfSpeech;
import com.example.englishapp.models.TranslationVariant;
import com.example.englishapp.models.Vocabulary;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TranslationDto {
    private Integer id;

    private PartOfSpeechDto partOfSpeech;

    private VocabularyDto vocabulary;

    private TranslationVariantDto translationVariant;

}

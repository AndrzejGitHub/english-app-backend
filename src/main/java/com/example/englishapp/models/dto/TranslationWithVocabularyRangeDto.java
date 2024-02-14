package com.example.englishapp.models.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TranslationWithVocabularyRangeDto {

    public TranslationDto translation;

    public VocabularyRangeDto vocabularyRange;
}

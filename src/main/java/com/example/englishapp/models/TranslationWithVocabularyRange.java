package com.example.englishapp.models;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class TranslationWithVocabularyRange {

    public TranslationWithVocabularyRange(Translation translation, VocabularyRange vocabularyRange) {
        this.translation = translation;
        this.vocabularyRange = vocabularyRange;
    }

    public Translation translation;
    public VocabularyRange vocabularyRange;

}

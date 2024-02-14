package com.example.englishapp.services;

import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TranslationService {
    List<Translation> getTranslations();

    Translation getTranslation(Integer id);

    List<Translation> getTranslationsByVocabularyId(Integer id);

    List<TranslationWithVocabularyRange> getTranslationsByVocabularyRangeIdWithVocabularyRange(Integer id);

    List<TranslationWithVocabularyRange> getTranslationsWithVocabularyRange();

    Translation insertTranslation(Translation translation);

    TranslationWithVocabularyRange insertTranslationWithVocabularyRange(TranslationWithVocabularyRange translationWithVocabularyRange);
    TranslationWithVocabularyRange updateTranslationWithVocabularyRange(TranslationWithVocabularyRange translationWithVocabularyRange, Integer id);
    @Transactional
    Translation updateTranslation(Translation translation, Integer id);

    @Transactional
    void removeTranslation(@PathVariable Integer id);

    @Transactional
    void removeTranslationAndVocabularyAndTranslationVariant(@PathVariable Integer id);

    @Transactional
    void removeTranslationAndVocabulary(@PathVariable Integer id);

    void removeTranslationsByPartOfSpeechId(Integer partOfSpeechId);

    void removeTranslationWithVocabularyAndTranslationVariantAndVocabularyRange(Integer id);


}

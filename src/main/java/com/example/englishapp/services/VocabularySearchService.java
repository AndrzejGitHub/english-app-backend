package com.example.englishapp.services;

import com.example.englishapp.models.Translation;
import com.example.englishapp.models.TranslationWithVocabularyRange;

import java.util.List;

public interface VocabularySearchService {
    List<Translation> searchVocabularyAndGiveResponseTranslations(String term);

    List<Translation> searchTranslationsByVocabularyOrderByPartOfSpeech(String term);

    List<Translation> searchTranslationsByEnglishWord(String term);

    List<Translation> searchTranslationsByRangeId(Integer rangeId);

    List<TranslationWithVocabularyRange> searchTranslationsWithVocabularyRangeByEnglishWord(String englishWord);
}
